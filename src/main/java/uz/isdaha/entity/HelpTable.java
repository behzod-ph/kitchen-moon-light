package uz.isdaha.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HelpTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(nullable = false)
    private String ip;

    private String url;

    private Integer response;

    @CreationTimestamp
    private Timestamp requestTimeStamp;

    public HelpTable(String ip) {
        this.ip = ip;
    }

    public String getRequestTimeStamp() {
        return requestTimeStamp.toString();
    }

    public HelpTable(String ip, String url, Integer response) {
        this.ip = ip;
        this.url = url;
        this.response = response;
    }
}
