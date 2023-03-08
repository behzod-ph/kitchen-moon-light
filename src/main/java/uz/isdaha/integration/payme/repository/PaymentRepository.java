package uz.isdaha.integration.payme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isdaha.integration.payme.entity.Payment;


import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findFirstByOrderTransactionIdOrderByPayDateDesc(Long orderTransactionId);


}
