package uz.isdaha.entity.lang;



import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "translation")
public class MultilingualString {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "lang_key")
    @CollectionTable(name = "translation_text", joinColumns = @JoinColumn(name = "translation_id"))
    private Map<String, LocalizedString> map = new HashMap<>();

    public MultilingualString() {
    }



    public String getText(String lang) {
        if (map.containsKey(lang)) {
            return map.get(lang).getText();
        }
        return null;
    }


    public Map<String, LocalizedString> getMap() {
        return map;
    }

    public void setMap(Map<String, LocalizedString> map) {
        this.map = map;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
