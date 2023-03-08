package uz.isdaha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.category.CategoryCreateRequest;
import uz.isdaha.payload.request.BranchRequest;
import uz.isdaha.payload.request.BranchUpdateRequest;
import uz.isdaha.service.branch.BranchService;
import uz.isdaha.service.category.CategoryService;


@RestController
@RequestMapping("api/v1/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;


    @PostMapping
    public ResponseEntity<?> add(@RequestPart(value = "images", required = false) MultipartFile[] images,
                                 @RequestPart(value = "request") BranchRequest request) {
        return branchService.create(request, images);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getByIdAdmin(@PathVariable("id") Long id) {
        return branchService.getByIdForAdmin(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdUser(@PathVariable("id") Long id) {
        return branchService.get(id);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllForAdmin(Pageable pageable) {
        return branchService.getAllForAdmin(pageable);
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return branchService.getAll();
    }


    @PutMapping("/admin/{id}")
    public ResponseEntity<?> edit(@RequestPart(value = "images", required = false) MultipartFile[] images,
                                  @RequestPart(value = "request") BranchUpdateRequest request, @PathVariable("id") long id) {
        return branchService.update(id, request, images);
    }

    @DeleteMapping("/admin//{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return branchService.delete(id);
    }


}
