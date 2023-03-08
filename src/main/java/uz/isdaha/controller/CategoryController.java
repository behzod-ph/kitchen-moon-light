package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.category.CategoryCreateRequest;
import uz.isdaha.service.category.CategoryService;


@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')  or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> add(
        @RequestPart(value = "icon", required = false) MultipartFile icon,
        @RequestPart(value = "categoryCreateRequest") CategoryCreateRequest categoryCreateRequest
    ) {
        return categoryService.add(categoryCreateRequest, icon);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getByIdAdmin(@PathVariable("id") Long id) {
        return categoryService.getByIdAdmin(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getByIdUser(@PathVariable("id") Long id) {
        return categoryService.getByIdUser(id);
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllForAdmin(Pageable pageable) {
        return categoryService.getAllAdmin(pageable);
    }

    @GetMapping("user/page")
    public ResponseEntity<?> getAllForUser(Pageable pageable) {
        return categoryService.getAllUser(pageable);
    }

    @GetMapping("category")
    public ResponseEntity<?> getByCategory(@RequestParam(value = "categoryId", required = false) Long categoryId) {
        return categoryService.getByCategory(categoryId);
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> edit(
        @PathVariable("id") Long id, @RequestPart(value = "icon", required = false) MultipartFile icon,
        @RequestPart(value = "categoryCreateRequest") CategoryCreateRequest categoryCreateRequest) {
        return categoryService.edit(categoryCreateRequest, icon, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return categoryService.delete(id);
    }


}
