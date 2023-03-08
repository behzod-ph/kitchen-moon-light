package uz.isdaha.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import uz.isdaha.entity.lang.LocalizedString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
public abstract class AuditableResponse<T> {

    private Map<String, LocalizedString> categoryName;

    private boolean isActive;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    private T createdBy;

    private T updatedBy;

}
