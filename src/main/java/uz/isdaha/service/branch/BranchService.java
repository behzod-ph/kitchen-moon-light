package uz.isdaha.service.branch;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.payload.request.BranchRequest;
import uz.isdaha.payload.request.BranchUpdateRequest;
import uz.isdaha.payload.response.BranchAdminResponse;
import uz.isdaha.service.CRUDService;

@Service
public interface BranchService<T, I>  {

    ResponseEntity<BranchAdminResponse> getByIdForAdmin(I id);

    ResponseEntity<?> getAllForAdmin(Pageable pageable);


    ResponseEntity<?> create(BranchRequest request, MultipartFile[] images);

    ResponseEntity<?> getAll();

    ResponseEntity<?> update(I id, BranchUpdateRequest request, MultipartFile[] images);

    ResponseEntity<?> get(I id);

    ResponseEntity<?> delete(I id);
}
