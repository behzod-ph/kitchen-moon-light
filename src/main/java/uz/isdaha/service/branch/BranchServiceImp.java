package uz.isdaha.service.branch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.constan.MessageConstants;
import uz.isdaha.entity.Branch;
import uz.isdaha.entity.BranchesImages;
import uz.isdaha.exception.BranchNotFoundException;
import uz.isdaha.payload.request.BranchRequest;
import uz.isdaha.payload.request.BranchUpdateRequest;
import uz.isdaha.payload.response.BranchAdminResponse;
import uz.isdaha.payload.response.BranchUserResponse;
import uz.isdaha.repository.BranchImagesRepository;
import uz.isdaha.repository.BranchRepository;
import uz.isdaha.service.FileService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImp implements BranchService<BranchUserResponse, Long> {


    private final BranchRepository branchRepository;

    private final BranchImagesRepository imagesRepository;

    private final FileService fileService;

    private final EntityManager entityManager;

    @Override
    public ResponseEntity<?> create(BranchRequest request, MultipartFile[] images) {
        Branch branch = request.toEntity(entityManager);

        List<BranchesImages> imagesList = new ArrayList<>();
        Arrays.stream(images).forEach(i -> {
            imagesList.add(new BranchesImages(branch, fileService.uploadFile(i)));
        });

        branch.setImages(imagesList);
        return ResponseEntity.ok(BranchAdminResponse.toDto(branchRepository.save(branch)));
    }

    @Override
    public ResponseEntity<?> get(Long id) {
        Branch branch = branchRepository.findByIdAndDeletedIsFalse(id).orElseThrow(BranchNotFoundException::new);
        return ResponseEntity.ok(BranchUserResponse.toDto(branch));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Branch branch = branchRepository.findByIdAndDeletedIsFalse(id).orElseThrow(BranchNotFoundException::new);
        branch.setDeleted(true);
        branchRepository.save(branch);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<?> update(Long id, BranchUpdateRequest request, MultipartFile[] images) {
        Branch branch = request.toEntity(entityManager);
        request.getDeletedImages().forEach(i -> {
            if (imagesRepository.existsByImage(i)) {
                fileService.delete(i);
                imagesRepository.deleteById(imagesRepository.getByImage(i).getId());
            }
        });

        if (images != null) {
            List<BranchesImages> imagesList = new ArrayList<>();
            Arrays.stream(images).forEach(i -> {
                imagesList.add(new BranchesImages(branch, fileService.uploadFile(i)));
            });
            branch.setImages(imagesList);
        }
        branch.setId(id);
        return ResponseEntity.ok(BranchAdminResponse.toDto(branchRepository.save(branch)));
    }

    @Override
    public ResponseEntity<BranchAdminResponse> getByIdForAdmin(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(BranchNotFoundException::new);
        return ResponseEntity.ok(BranchAdminResponse.toDto(branch));
    }

    @Override
    public ResponseEntity<?> getAllForAdmin(Pageable pageable) {
        return ResponseEntity.ok(branchRepository.findAll(pageable).stream().map(BranchAdminResponse::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(branchRepository.findAll().stream().map(BranchUserResponse::toDto).collect(Collectors.toList()));
    }
}
