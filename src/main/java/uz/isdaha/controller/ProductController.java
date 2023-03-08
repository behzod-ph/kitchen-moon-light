package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.request.ProductRequest;
import uz.isdaha.service.product.ProductService;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // -----------------  USER  ------------------------ //
    @GetMapping("favorites")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getFavouriteProduct() {
        return ResponseEntity.ok(productService.getAllFavourites());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return productService.get(id);
    }

    @GetMapping("category/{category_id}")
    public ResponseEntity<?> getByCategory(@PathVariable("category_id") Long id) {
        return productService.getProductsByCategory(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        return productService.getAllByPage(pageable);
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/favorite/{productId}")
    public ResponseEntity<?> addFavouriteProduct(@PathVariable long productId) {
        return ResponseEntity.ok(productService.addFavouriteProduct(productId));

    }


    // ---------------------------  Admin -----------------//

    @GetMapping("admin")
    public ResponseEntity<?> getAllForAdmin(Pageable pageable) {
        return productService.getAllForAdmin(pageable);
    }


    @GetMapping("admin/{id}")
    public ResponseEntity<?> getByIdAdmin(@PathVariable("id") Long id) {
        return productService.getByIdAdmin(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> add(
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") ProductRequest request) {
        return productService.add(request, image);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> edit(
        @PathVariable("id") Long id,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") ProductRequest request
    ) {
        return productService.update(id, request, image);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return productService.delete(id);
    }



}

