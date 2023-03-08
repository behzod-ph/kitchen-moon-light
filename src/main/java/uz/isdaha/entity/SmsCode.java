package uz.isdaha.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Entity(name = "sms_code")
@Table(indexes = @Index(columnList = "phone_number"))
public class SmsCode extends BaseEntity<String> {

    @Column(nullable = false)
    private String code;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean checked = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean ignored = false;

}
