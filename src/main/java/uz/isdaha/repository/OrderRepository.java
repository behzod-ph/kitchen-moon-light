package uz.isdaha.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isdaha.entity.Order;
import uz.isdaha.entity.User;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.enums.OrderType;
import uz.isdaha.enums.PaymentMethod;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.DoubleStream;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status, Pageable pageable);

    List<Order> findByUserAndDeletedFalse(User user);

    List<Order> findByCreatedAtBetweenOrPaymentMethodOrStatus(Timestamp createdAtStart, Timestamp createdAtEnd, PaymentMethod paymentMethod, OrderStatus status, Pageable pageable);


    List<Order> findByCreatedAtBetween(Timestamp createdAtStart, Timestamp createdAtEnd);


    List<Order> findByOrderType(OrderType orderType, Pageable pageable);

    List<Order> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);

    List<Order> findByPaymentMethodAndOrderType(PaymentMethod paymentMethod, OrderType orderType, Pageable pageable);
}
