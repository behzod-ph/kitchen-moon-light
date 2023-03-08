package uz.isdaha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.isdaha.entity.SmsCode;

import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    @Query(value = "SELECT :limitSms <= (SELECT COUNT(*)\n" +
        "                     FROM sms_code\n" +
        "                     WHERE phone_number = :phoneNumber\n" +
        "                       AND created_at > (SELECT current_timestamp - (INTERVAL '1' hour) * :limitHour) AND ignored=false)", nativeQuery = true)
    boolean overLimitSmsCodeByPhoneNumberAndDuration(@Param("limitSms") Integer limitSms, @Param("phoneNumber") String phoneNumber, @Param("limitHour") Integer limitHour);

    Optional<SmsCode> findFirstByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);
}
