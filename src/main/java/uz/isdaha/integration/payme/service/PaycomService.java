package uz.isdaha.integration.payme.service;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.isdaha.integration.payme.entity.Payment;
import uz.isdaha.integration.payme.entity.enums.TransactionState;
import uz.isdaha.integration.payme.payload.*;
import uz.isdaha.integration.payme.repository.OrderTransactionRepository;
import uz.isdaha.integration.payme.repository.PaymentRepository;
import uz.isdaha.integration.payme.entity.OrderTransaction;
import uz.isdaha.entity.Order;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.repository.OrderRepository;
import uz.isdaha.util.Utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

import static uz.isdaha.integration.payme.service.PaymeConstans.*;

@Service
@RequiredArgsConstructor
public class PaycomService implements IPaycomService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderTransactionRepository orderTransactionRepository;

    private Logger logger = Logger.getLogger(String.valueOf(PaycomService.class));

    @Value("${payme.url}")
    private String url;

    @Value("${payme.merchantId}")
    private String merchantId;

    @Override
    public JSONObject payWithPaycom(PaycomRequestForm requestForm, String authorization) {
        Params params = requestForm.getParams();
        JSONRPC2Response response = new JSONRPC2Response(params.getId());
        logger.info(requestForm.toString());

        switch (requestForm.getMethod()) {
            case CHECK_PERFORM_TRANSACTION:
                checkPerformTransaction(requestForm, response);
                break;
            case CREATE_TRANSACTION:
                createTransaction(requestForm, response);
                break;
            case PERFORM_TRANSACTION:
                performTransaction(requestForm, response);
                break;
            case CANCEL_TRANSACTION:
                cancelTransaction(requestForm, response);
                break;
            case CHECK_TRANSACTION:
                checkTransaction(requestForm, response);
                break;
            case GET_STATEMENT:
                getStatement(requestForm, response);
                break;
        }
        logger.info(response.toString());
        response.setID(requestForm.getId());
        return response.toJSONObject();
    }


    public boolean checkPerformTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {

        if (requestForm.getParams().getAccount() == null) {
            response.setError(new JSONRPC2Error(-31050, ACCOUNT_FIELD_NOT_FOUND, ACCOUNT));
            return false;
        }

        if (requestForm.getParams().getAccount().getOrder() == null) {
            response.setError(new JSONRPC2Error(-31050, ORDER_NOT_FOUND, ORDER));
            return false;
        }

        if (requestForm.getParams().getAmount() == null || requestForm.getParams().getAmount().intValue() == 0) {
            response.setError(new JSONRPC2Error(-31001, AMOUNT_ERROR_NULL, AMOUNT));
            return false;
        }

        Optional<Order> optionalOrder = orderRepository.findById(requestForm.getParams().getAccount().getOrder());

        if (optionalOrder.isEmpty()) {
            response.setError(new JSONRPC2Error(-31050, ORDER_NOT_FOUND, ORDER));
            return false;
        }

        Order order = optionalOrder.get();
        if (!((order.getTotalPrice().doubleValue() * 100) == requestForm.getParams().getAmount().intValue())) {
            response.setError(new JSONRPC2Error(-31001, AMOUNT_WRONG, AMOUNT));
            return false;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            response.setError(new JSONRPC2Error(-31099, ORDER_ALREADY_CANCELLED, ORDER));
            return false;
        }

        if (order.isPaid()) {
            response.setError(new JSONRPC2Error(-31099, ORDER_ALREADY_FINISHED, ORDER));
            return false;
        }

        Detail detail = new Detail();
        List<Items> items = new ArrayList<>();
        order.getProducts().stream().distinct().forEach(i -> items.add(Items.builder()
            .title(i.getProduct().getProductName())
            .code(i.getProduct().getCode())
            .count(i.getCount())
            .packageCode(order.getId().toString())
            .price(i.getProduct().getPrice().intValue() * 100)
            .vatPercent((int) (i.getProduct().getPrice().intValue() * 100 * 0.12))
            .build()));
        detail.setItems(items);
        response.setID(requestForm.getParams().getId());
        response.setResult(new CheckPerformTransactionAllowResponse(
            new AdditionalInfo(order.getId(), order.getTotalPrice().intValue()),
            true, detail));
        return true;
    }

    private void createTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {

        Optional<OrderTransaction> optionalOrderTransaction = orderTransactionRepository.findByTransactionId(requestForm.getParams().getId());

        OrderTransaction orderTransaction;

        if (optionalOrderTransaction.isPresent()) {
            orderTransaction = optionalOrderTransaction.get();

            if (!orderTransaction.getState().equals(TransactionState.STATE_IN_PROGRESS.getCode())) {
                response.setError(new JSONRPC2Error(-31008, UNABLE_TRANSACTION, TRANSACTION));
                return;
            }

            if (System.currentTimeMillis() - orderTransaction.getTransactionCreationTime().getTime() > TIME_EXPIRED_PAYCOM_ORDER) {
                response.setError(new JSONRPC2Error(-31008, UNABLE_TRANSACTION, TRANSACTION));
                orderTransaction.setReason(4);
                orderTransaction.setState(TransactionState.STATE_CANCELED.getCode());
                orderTransactionRepository.save(orderTransaction);
                return;
            }
        } else {
            boolean checkPerformTransaction = checkPerformTransaction(requestForm, response);

            if (!checkPerformTransaction) return;

            orderTransaction = new OrderTransaction(
                requestForm.getParams().getId(),
                new Timestamp(requestForm.getParams().getTime()),
                TransactionState.STATE_IN_PROGRESS.getCode(),
                requestForm.getParams().getAccount().getOrder());
            orderTransactionRepository.save(orderTransaction);
        }
        ResultForm resultForm = ResultForm.builder()
            .createTime(orderTransaction.getTransactionCreationTime().getTime())
            .state(orderTransaction.getState())
            .transaction(orderTransaction.getId().toString())
            .build();

        response.setResult(resultForm);
    }


    private void performTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {

        Optional<OrderTransaction> optionalOrderTransaction = orderTransactionRepository.findByTransactionId(requestForm.getParams().getId());

        if (optionalOrderTransaction.isEmpty()) {
            response.setError(new JSONRPC2Error(-31003, TRANSACTION_ORDER_NOT_FOUND, TRANSACTION));
            return;
        }
        OrderTransaction orderTransaction = optionalOrderTransaction.get();

        if (orderTransaction.getState().equals(TransactionState.STATE_IN_PROGRESS.getCode())) {
            if (System.currentTimeMillis() - orderTransaction.getTransactionCreationTime().getTime() > TIME_EXPIRED_PAYCOM_ORDER) {
                response.setError(new JSONRPC2Error(-31008, UNABLE_TRANSACTION, TRANSACTION));
                orderTransaction.setReason(4);
                orderTransaction.setState(TransactionState.STATE_CANCELED.getCode());
                orderTransactionRepository.save(orderTransaction);
                return;
            }

            orderTransaction.setState(TransactionState.STATE_DONE.getCode());
            orderTransaction.setPerformTime(new Timestamp(System.currentTimeMillis()));
            orderTransactionRepository.save(orderTransaction);

            Payment payment = new Payment(
                orderTransaction.getOrder().getUser(),
                orderTransaction.getOrder().getTotalPrice().doubleValue(),
                new Timestamp(System.currentTimeMillis()),
                orderTransaction.getId(),
                orderTransaction.getTransactionId());
            paymentRepository.save(payment);

            Order order = orderTransaction.getOrder();
            order.setPaid(true);
            orderRepository.save(order);
        }

        if (orderTransaction.getState().equals(TransactionState.STATE_DONE.getCode())) {
            response.setResult(new ResultForm(
                orderTransaction.getPerformTime().getTime(),
                orderTransaction.getState(),
                orderTransaction.getId().toString()));
            return;
        }
        response.setError(new JSONRPC2Error(-31008, UNABLE_TRANSACTION, TRANSACTION));
    }


    private void cancelTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {
        Optional<OrderTransaction> optionalOrderTransaction = orderTransactionRepository.findByTransactionId(requestForm.getParams().getId());

        if (optionalOrderTransaction.isEmpty()) {
            response.setError(new JSONRPC2Error(-31003, TRANSACTION_ORDER_NOT_FOUND, TRANSACTION));
            return;
        }

        OrderTransaction orderTransaction = optionalOrderTransaction.get();

        if (orderTransaction.getState().equals(TransactionState.STATE_IN_PROGRESS.getCode())) {
            orderTransaction.setState(TransactionState.STATE_CANCELED.getCode());
            orderTransaction.setReason(requestForm.getParams().getReason());
            orderTransaction.setCancelTime(new Timestamp(System.currentTimeMillis()));
            orderTransactionRepository.save(orderTransaction);

            ResultForm resultForm = ResultForm.builder()
                .cancelTime(orderTransaction.getCancelTime().getTime())
                .state(orderTransaction.getState())
                .transaction(orderTransaction.getId().toString())
                .build();

            response.setResult(resultForm);
            return;
        }

        if (orderTransaction.getState().equals(TransactionState.STATE_DONE.getCode())) {
            Order order = orderTransaction.getOrder();
            if (order.getStatus() != OrderStatus.DELIVER) {
                Optional<Payment> optionalPayment = paymentRepository.findFirstByOrderTransactionIdOrderByPayDateDesc(orderTransaction.getId());

                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();
                    payment.setCancelled(true);
                    paymentRepository.save(payment);

                    order.setPaid(false);
                    orderRepository.save(order);
                    orderTransaction.setState(TransactionState.STATE_POST_CANCELED.getCode());
                    orderTransaction.setReason(requestForm.getParams().getReason());
                    orderTransaction.setCancelTime(new Timestamp(System.currentTimeMillis()));
                    orderTransactionRepository.save(orderTransaction);

                    ResultForm resultForm = ResultForm.builder()
                        .cancelTime(orderTransaction.getCancelTime().getTime())
                        .state(orderTransaction.getState())
                        .transaction(orderTransaction.getId().toString())
                        .build();

                    response.setResult(resultForm);
                } else {
                    response.setError(new JSONRPC2Error(-31007, UNABLE_CANCEL_TRANSACTION, TRANSACTION));
                    return;
                }
            } else {
                response.setError(new JSONRPC2Error(-31007, ORDER_ALREADY_DELIVERED, ORDER));
                return;

            }
        }
        ResultForm resultForm = ResultForm.builder()
            .cancelTime(orderTransaction.getCancelTime().getTime())
            .state(orderTransaction.getState())
            .transaction(orderTransaction.getId().toString())
            .build();

        response.setResult(resultForm);
    }


    private void checkTransaction(PaycomRequestForm requestForm, JSONRPC2Response response) {
        Optional<OrderTransaction> optionalOrderTransaction = orderTransactionRepository.findByTransactionId(requestForm.getParams().getId());

        if (optionalOrderTransaction.isEmpty()) {
            response.setError(new JSONRPC2Error(-31003, TRANSACTION_ORDER_NOT_FOUND, TRANSACTION));
            return;
        }

        OrderTransaction orderTransaction = optionalOrderTransaction.get();

        response.setResult(new ResultForm(
            orderTransaction.getCancelTime() != null ? orderTransaction.getCancelTime().getTime() : 0,
            orderTransaction.getTransactionCreationTime().getTime(),
            orderTransaction.getPerformTime() != null ? orderTransaction.getPerformTime().getTime() : 0,
            orderTransaction.getReason(),
            orderTransaction.getState(),
            orderTransaction.getId().toString()));
    }


    private void getStatement(PaycomRequestForm requestForm, JSONRPC2Response response) {

        List<OrderTransaction> orderTransactionList =
            orderTransactionRepository.findAllByStateAndTransactionCreationTimeBetween(TransactionState.STATE_DONE.getCode(), new Timestamp(requestForm.getParams().getFrom()), new Timestamp(requestForm.getParams().getTo()));

        List<Transaction> transactions = new ArrayList<>();

        for (OrderTransaction orderTransaction : orderTransactionList) {
            Transaction transaction = new Transaction(
                orderTransaction.getTransactionId(),
                new Account(orderTransaction.getOrderId()),
                orderTransaction.getOrder().getTotalPrice().intValue(),
                0L,
                orderTransaction.getTransactionCreationTime().getTime(),
                orderTransaction.getPerformTime().getTime(),
                null,
                orderTransaction.getState(),
                orderTransaction.getTransactionCreationTime().getTime(),
                orderTransaction.getId().toString());
            transactions.add(transaction);
        }
        response.setResult(transactions);
    }

    public String generateUrl(Long orderId, BigDecimal amount) {
        return url + Utils.encode("m=" + merchantId + ";" +
            "ac.order_id=" + orderId + ";" +
            "a=" + amount.multiply(BigDecimal.valueOf(100)).intValue());
    }

}
