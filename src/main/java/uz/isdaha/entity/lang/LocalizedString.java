package uz.isdaha.entity.lang;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Data
public class LocalizedString {
    private String lang;
    @Column(columnDefinition = "text")
    private String text;

    public LocalizedString(String lang, String text) {
        this.lang = lang;
        this.text = text;
    }

    public LocalizedString() {
    }
}
