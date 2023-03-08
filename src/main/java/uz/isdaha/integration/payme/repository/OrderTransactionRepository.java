package uz.isdaha.integration.payme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isdaha.integration.payme.entity.OrderTransaction;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {

    Optional<OrderTransaction> findByTransactionId(String transactionId);

    List<OrderTransaction> findAllByStateAndTransactionCreationTimeBetween(Integer state, Timestamp fromTransactionCreationTime, Timestamp toTransactionCreationTime);
}
