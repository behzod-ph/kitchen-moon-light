package uz.isdaha.service.category;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.category.CategoryCreateRequest;
import uz.isdaha.service.CRUDService;
import uz.isdaha.service.PageProducer;


public interface CategoryService extends CRUDService<CategoryCreateRequest, CategoryCreateRequest, Long> {


    ResponseEntity<?> add(CategoryCreateRequest categoryCreateRequest, MultipartFile icon);

    ResponseEntity<?> getByIdUser(Long id);

    ResponseEntity<?> getByIdAdmin(Long id);

    ResponseEntity<?> getAllUser(Pageable pageable);

    ResponseEntity<?> getByCategory(Long categoryId);

    ResponseEntity<?> getAllAdmin(Pageable pageable);

    ResponseEntity<?> edit(CategoryCreateRequest categoryCreateRequest, MultipartFile icon, long  id);



}
