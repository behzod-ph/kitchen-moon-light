package uz.isdaha.integration.click.service;


import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.isdaha.entity.Order;
import uz.isdaha.entity.User;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.integration.click.entity.ClickObject;
import uz.isdaha.integration.click.payload.ClickDTO;
import uz.isdaha.integration.click.repository.ClickObjectRepository;
import uz.isdaha.integration.payme.entity.Payment;
import uz.isdaha.integration.payme.repository.PaymentRepository;
import uz.isdaha.repository.OrderRepository;
import uz.isdaha.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClickService {

    private final OrderRepository orderRepository;

    private final ClickObjectRepository clickObjectRepository;

    private final PaymentRepository paymentRepository;

    private final UserRepository clientRepository;

    private final String secureKey = "3ArijpZ8pWsUa";

    public ClickDTO prepareMethod(ClickDTO clickDTO) {
        //SECURE KEY BILAN BIRGALIKDA MD5 YASALAYAPTI
        String signKey = DigestUtils.md5Hex(clickDTO.getClickTransId().toString() +
            clickDTO.getServiceId().toString() +
            secureKey +
            clickDTO.getMerchantTransId() +
            Math.round(clickDTO.getAmount()) +
            clickDTO.getAction().toString() +
            clickDTO.getSignTime());

        //CLICK GA QAYTARISH UCHUN PREPARE OBJECT YASAYAPMIZ
        ClickObject clickObject = new ClickObject();
        clickObject.setClickTransId(clickDTO.getClickTransId());
        clickObject.setAmount(clickDTO.getAmount());

        //AGAR SECURE KEY XATO BERGAN BO'LSA
        if (!clickDTO.getSignString().equals(signKey)) {
            clickObject.setError(-1);
            clickObject.setErrorNote("SIGN CHECK FAILED!");
        }
        //SIGN KEY TO'G'RI BO'LSA
        else {
            //FOYDALANUVCHILAR RO'YXATIDAN CLICK NI TOPYAPMIZ
            Optional<User> optionalClickUser = clientRepository.findByPhoneNumber("Click");

            //AGAR CUNDAY USER BOR BO'LSA
            if (optionalClickUser.isPresent()) {

                //TIZIMGA CLICK KIRDI AUTH GA SET QILYAPMIZ
                Authentication authentication = new UsernamePasswordAuthenticationToken(optionalClickUser.get(), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                try {

                    //CLICK DAN KELAYOTGAN ORDER ID KELADI(AGAR SIZDA ABONENTSKIY UCHUN TO'LAYOTGAN BO'LSA TELEFON RAQAM KELADI)
                    String orderId = clickDTO.getMerchantTransId();

                    //ORDER NI OLAMIZ DB DAN
                    Optional<Order> optionalOrder = orderRepository.findById(Long.parseLong(orderId));

                    if (optionalOrder.isEmpty()) {
                        clickObject.setError(-5);
                        clickObject.setErrorNote("Order does not exist");
                    } else {

                        //AGAR ORDER UCHUN TO'LOV QILAYOTGAN BO'LSA TO'LANAYOTGAN SUMMA ORDER SUMMASIGA TENG BO'LISHI KERAK
                        if (optionalOrder.get().getTotalPrice().floatValue() == clickDTO.getAmount()) {
                            clickObject.setError(0);
                            clickObject.setErrorNote("SUCCESS");
                        } else {
                            clickObject.setError(-2);
                            clickObject.setErrorNote("Incorrect parameter amount");
                        }

//                        AGAR SIZDA ABONENTSIKIY UCHUN TO'LOV QILSA QUYIDAGI 2 QATORNI OCHASIZ, YUQORIDA KODNI COMMENT QILASIZ
//                        prepareObject.setError(0);
//                        prepareObject.setErrorNote("SUCCESS");
                    }
                } catch (Exception e) {
                    clickObject.setError(-5);
                    clickObject.setErrorNote("Order does not exist");
                }
            }
        }

        ClickDTO response = new ClickDTO();

        if (clickObject.getError() == 0) {
            clickObject = clickObjectRepository.save(clickObject);

            response.setMerchantPrepareId(clickObject.getId());
        }
        response.setClickTransId(clickDTO.getClickTransId());
        response.setMerchantTransId(clickDTO.getMerchantTransId());
        response.setError(clickObject.getError());
        response.setErrorNote(clickObject.getErrorNote());

        return clickDTO;
    }

    public ClickDTO completeMethod(ClickDTO clickDTO) {

        String signKey = DigestUtils.md5Hex(clickDTO.getClickTransId().toString() +
            clickDTO.getServiceId().toString() +
            secureKey +
            clickDTO.getMerchantTransId() +
            clickDTO.getMerchantPrepareId().toString() +
            Math.round(clickDTO.getAmount()) +
            clickDTO.getAction().toString() +
            clickDTO.getSignTime());

        ClickDTO response = new ClickDTO();
        response.setClickTransId(clickDTO.getClickTransId());
        response.setMerchantTransId(clickDTO.getMerchantTransId());

        //SIGN KEY XATO BO'LGANDA
        if (!clickDTO.getSignString().equals(signKey)) {
            response.setError(-1);
            response.setErrorNote("SIGN CHECK FAILED!");
        } else {
            //FOYDALANUVCHILAR RO'YXATIDAN CLICK NI TOPYAPMIZ
            Optional<User> optionalClickUser = clientRepository.findByPhoneNumber("Click");

            //AGAR BUNDAY FOYDALANIVCHI BO'LMASA
            if (optionalClickUser.isEmpty()) {
                response.setError(-1);
                response.setErrorNote("SIGN CHECK FAILED!");
            } else {

                //TIZIMGA CLICK KIRDI AUTH GA SET QILYAPMIZ
                Authentication authentication = new UsernamePasswordAuthenticationToken(optionalClickUser.get(), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                Optional<ClickObject> optionalClickObject = clickObjectRepository.findById(clickDTO.getMerchantPrepareId());

                //PREPARE METHODGA AVVAL KELMAGAN YOKI PREPARE METHOD DA ERROR BILAN QAYTARILGANDA
                if (optionalClickObject.isEmpty()) {
                    response.setError(-6);
                    response.setErrorNote("Transactions does not exist");
                } else {

                    //TRANSACTION AVVAL BEKOR QILINGAN HOLATDA
                    if (optionalClickObject.get().isCancelled()) {
                        response.setError(-9);
                        response.setErrorNote("Transaction cancelled");
                    } else {

                        //ORDER BO'YICHA TEKSHIRIHSNI BOSHLAYMIZ
                        try {
                            //CLICK DAN KELAYOTGAN ORDER ID KELADI(AGAR SIZDA ABONENTSKIY UCHUN TO'LAYOTGAN BO'LSA TELEFON RAQAM KELADI)
                            String orderId = clickDTO.getMerchantTransId();

                            Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(orderId));

                            if (orderOptional.isEmpty()) {
                                response.setError(-5);
                                response.setErrorNote("Order not found!");
                            } else {
                                Order order = orderOptional.get();

                                if (order.getStatus() == OrderStatus.CANCELLED) {
                                    response.setError(-9);
                                    response.setErrorNote("Transactions cancelled");
                                } else if (order.isPaid()) {
                                    response.setError(-4);
                                    response.setErrorNote("Already paid");
                                } else {
                                    if (order.getTotalPrice().floatValue() == clickDTO.getAmount() && clickDTO.getError() == 0) {

                                        //ENDI BIZ PAYMENT TABLE GA TO'LOV BO'LDI DEB BELGILAB QO'YAMIZ
                                        Payment payment = new Payment(
                                            order.getUser(),
                                            order.getTotalPrice().doubleValue(),
                                            new Timestamp(System.currentTimeMillis()),
                                            1L,
                                            clickDTO.getClickTransId().toString());

                                        paymentRepository.save(payment);
                                        order.setPaid(true);

                                        response.setError(0);
                                        response.setErrorNote("SUCCESS");

                                        response.setMerchantConfirmId(optionalClickObject.get().getId());
                                    } else {
                                        response.setError(-2);
                                        response.setErrorNote("Incorrect parameter amount");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            response.setError(-9);
                            response.setErrorNote("Transactions cancelled");
                        }
                    }
                }
            }
        }
        return response;
    }
}
