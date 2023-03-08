package uz.isdaha.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.Creatable;
import uz.isdaha.payload.Modifiable;

public interface CRUDService<C extends Creatable, M extends Modifiable, I> {
    ResponseEntity<?> create(C c);

    ResponseEntity<?> get(I id);

    ResponseEntity<?> delete(I id);

    ResponseEntity<?> update(I id, M m);
}
