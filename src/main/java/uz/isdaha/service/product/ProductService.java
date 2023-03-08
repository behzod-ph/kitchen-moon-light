package uz.isdaha.service.product;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.request.ProductPageableRequest;
import uz.isdaha.payload.request.ProductRequest;

public interface ProductService {

    ResponseEntity<?> get(Long id);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> update(Long id, ProductRequest productRequest, MultipartFile image);

    ResponseEntity<?> getProductsByCategory(long categoryId);

    ResponseEntity<?> add(ProductRequest categoryCreateRequest, MultipartFile image);

    ResponseEntity<?> getAllForAdmin(Pageable pageable);

    ResponseEntity<?> getAllByPage(Pageable pageable);

    ResponseEntity<?> addFavouriteProduct(long productId);

    ResponseEntity<?> getAllFavourites();

    ResponseEntity<?> getByIdAdmin(Long id);

    ResponseEntity<?> getAllByProperties(ProductPageableRequest pageableRequest);
}
