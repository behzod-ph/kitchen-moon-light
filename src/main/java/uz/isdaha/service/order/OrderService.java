package uz.isdaha.service.order;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import uz.isdaha.bots.OrderBot;
import uz.isdaha.entity.*;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.enums.OrderType;
import uz.isdaha.enums.PaymentMethod;
import uz.isdaha.enums.RoleEnum;
import uz.isdaha.exception.AddressNotFoundException;
import uz.isdaha.exception.BranchNotFoundException;
import uz.isdaha.exception.OrderNotFoundException;
import uz.isdaha.exception.ProductNotFoundException;
import uz.isdaha.integration.payme.service.PaycomService;
import uz.isdaha.payload.request.OrderPageableRequest;
import uz.isdaha.payload.request.OrderRequest;
import uz.isdaha.payload.request.OrderUpdateRequest;
import uz.isdaha.payload.request.ProductCount;
import uz.isdaha.payload.response.Message;
import uz.isdaha.payload.response.OrderAdminResponse;
import uz.isdaha.payload.response.OrderCheckResponse;
import uz.isdaha.payload.response.OrderResponse;
import uz.isdaha.repository.*;
import uz.isdaha.service.CRUDService;
import uz.isdaha.service.QRCodeService;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static uz.isdaha.constan.RestApiConstants.*;

@Service
@RequiredArgsConstructor
public class OrderService implements CRUDService<OrderRequest, OrderUpdateRequest, Long> {

    private final OrderRepository orderRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ProductRepository productRepository;

    private final ResourceLoader resourceLoader;

    private final SimpMessagingTemplate messageTemplate;

    private final FeeDistanceRepository feeDistanceRepository;

    private final BranchRepository branchRepository;

    private final ProductCountRepository countRepository;

    private final OrderBot bot;

    private final PaycomService paycomService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> create(OrderRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<ProductCountOrder> productCounts = new HashSet<>();
        for (ProductCount pc : request.getProducts()) {
            Product product = productRepository.findById(pc.getProductId()).orElseThrow(ProductNotFoundException::new);
            productCounts.add(new ProductCountOrder(product, pc.getCount()));
        }
        FeeDistance feeDistance = feeDistanceRepository.findByOrderByCreatedAtDesc().stream().findFirst().orElse(new FeeDistance(BigDecimal.ZERO, BigDecimal.ZERO));

        Order order = Order.builder()
            .branch(branchRepository.findById(request.getBranchId()).orElseThrow(BranchNotFoundException::new))
            .address(addressRepository.findById(request.getAddressId()).orElseThrow(AddressNotFoundException::new))
            .deliveryTime(new Date(request.getDeliveryTime()))
            .distance(request.getDistance())
            .paymentMethod(request.getPaymeType())
            .status(OrderStatus.ORDERED)
            .orderType(request.getOrderType())
            .user(currentUser)
            .comment(request.getComment())
            .build();

        order.setProducts(productCounts);

        if (request.getOrderType() == OrderType.DELIVERY) {
            order.setTotalPrice(sumProductPrice(productCounts).add(feeDistance.getPrice().multiply(BigDecimal.valueOf(request.getDistance()))));
        } else {
            order.setTotalPrice(sumProductPrice(productCounts));
        }
        order = orderRepository.save(order);


        Role role = roleRepository.findByRole(RoleEnum.OPERATOR).orElse(new Role(RoleEnum.OPERATOR));

        Order finalOrder = order;
        productCounts.forEach(i -> {
            i.setOrder(finalOrder);
        });
        countRepository.saveAll(productCounts);
        userRepository.findByRoleAndDeletedFalse(role).stream().forEach(op -> {
            Message msg = new Message(finalOrder.getId(), finalOrder.getUser().getPhoneNumber(), finalOrder.getPaymentMethod());
            messageTemplate.convertAndSendToUser(op.getPhoneNumber(), "/topic/new-order", msg);// todo create message dto

        });

        bot.sendNotification(order, false);
        OrderResponse response = OrderResponse.toDto(order);
        if (order.getPaymentMethod() == PaymentMethod.PAYME) {
            response.setRedirectUrl(paycomService.generateUrl(order.getId(), order.getTotalPrice()));
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> get(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        return ResponseEntity.ok(OrderResponse.toDto(order));
    }

    public ResponseEntity<?> getByIdForCheck(long id) throws IOException, WriterException {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        return ResponseEntity.ok(OrderCheckResponse.toDto(order, generateQrCode(order.getAddress().getLatitude(), order.getAddress().getLongitude())));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        // delete qilib bo'lmaydi
        return null;
    }

    @Override
    public ResponseEntity<?> update(Long id, OrderUpdateRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        order.setStatus(request.status);
        bot.sendNotification(order, true);
        messageTemplate.convertAndSendToUser(order.getUser().getPhoneNumber(), "topic/change", request.status);
        return ResponseEntity.ok(OrderAdminResponse.toDto(orderRepository.save(order)));
    }


    public ByteArrayInputStream reportOrder(Timestamp fromDate, Timestamp toDate) throws IOException {
        Resource resource = resourceLoader.getResource(TEMPLATE_PATH);
        File file = resource.getFile();
        List<Order> orders = orderRepository.findByCreatedAtBetween(fromDate, toDate);
        // todo
        FileInputStream excelFile = new FileInputStream("/Users/ismoilovdavron/Documents/Documents/task/Moti Panasian /src/main/resources/reports/template.xlsx");
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        int i = 2;
        for (Order order : orders) {
            Row row = datatypeSheet.getRow(i);
            Cell cell = row.getCell(1);
            cell.setCellValue(order.getId());

            cell = row.getCell(2);
            cell.setCellValue(order.getUser().getPhoneNumber());

            cell = row.getCell(3);
            cell.setCellValue(order.getCreatedAt());

            cell = row.getCell(4);
            cell.setCellValue(order.getDeliveryTime());


            cell = row.getCell(5);
            cell.setCellValue(order.getStatus().name());

            cell = row.getCell(6);
            cell.setCellValue(sumProductPrice(order.getProducts()).toString());
            i++;

        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());

    }


    private BigDecimal sumProductPrice(Set<ProductCountOrder> countOrders) {
        BigDecimal sum = new BigDecimal(0);
        for (ProductCountOrder pc : countOrders) {
            sum = sum.add(pc.getProduct().getPrice().multiply(BigDecimal.valueOf(pc.getCount())));
        }
        return sum;
    }


    private byte[] generateQrCode(double latitude, double longtitude) throws IOException, WriterException {
        String data = QR_CODE_API + latitude + "," + longtitude;
        return QRCodeService.getQRCodeImage(data);
    }


    public ResponseEntity<?> history() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(orderRepository.findByUserAndDeletedFalse(currentUser).stream().map(OrderResponse::toDto).collect(Collectors.toList())
        );
    }

    public ResponseEntity<?> getAll(OrderPageableRequest request) {
        final PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());
        if (request.getSortBy() != null && request.getSortBy().length != 0) {
            pageRequest.withSort(
                request.getDirection() != null ? Sort.Direction.valueOf(request.getDirection()) : DESC,
                request.getSortBy()
            );
        }


        if (request.getOrderType() != null && request.getMethod() != null) {
            return ResponseEntity.ok(orderRepository.findByPaymentMethodAndOrderType(
                request.getMethod(),
                request.getOrderType(),
                pageRequest
            ).stream().map(OrderAdminResponse::toDto));
        } else if (request.getMethod() != null) {
            return ResponseEntity.ok(orderRepository.findByPaymentMethod(
                request.getMethod(),
                pageRequest
            ).stream().map(OrderResponse::toDto));
        } else if (request.getOrderType() != null) {
            return ResponseEntity.ok(orderRepository.findByOrderType(
                request.getOrderType(),
                pageRequest
            ).stream().map(OrderAdminResponse::toDto));
        } else {
            return ResponseEntity.ok(orderRepository.findAll().stream().map(OrderAdminResponse::toDto));
        }

    }
}
