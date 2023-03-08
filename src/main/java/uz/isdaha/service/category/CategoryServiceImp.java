package uz.isdaha.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.Category;
import uz.isdaha.entity.lang.MultilingualString;
import uz.isdaha.exception.CategoryNotFoundException;
import uz.isdaha.payload.category.CategoryAdminDto;
import uz.isdaha.payload.category.CategoryCreateRequest;
import uz.isdaha.payload.category.CategoryDtoUser;
import uz.isdaha.repository.CategoryRepository;
import uz.isdaha.service.FileService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final FileService fileService;

    @Override
    public ResponseEntity<?> add(CategoryCreateRequest request, MultipartFile icon) {
        Category parentCategory = null;
        if (request.getSubcategoryId() != null) {
            parentCategory = categoryRepository.findById(request.getSubcategoryId()).orElseThrow(CategoryNotFoundException::new);
        }


        Category category = new Category();
        category.setParentCategory(parentCategory);

        if (icon != null) {
            String url = fileService.uploadFile(icon);
            category.setIconUrl(url);
        }
        MultilingualString multilingualString = new MultilingualString();
        multilingualString.setMap(request.getCategoryName());
        category.setCategoryName(multilingualString);
        category = categoryRepository.save(category);
        return ResponseEntity.ok(CategoryAdminDto.toDto(category));
    }

    @Override
    public ResponseEntity<?> create(CategoryCreateRequest categoryCreateRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> get(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        return ResponseEntity.ok(category);
    }


    @Override
    public ResponseEntity<?> delete(Long id) {
        getByIdUser(id);
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> update(Long id, CategoryCreateRequest request) {
        Category parentCategory = null;
        if (request.getSubcategoryId() != null) {
            parentCategory = categoryRepository.findById(request.getSubcategoryId()).orElseThrow(CategoryNotFoundException::new);
        }
        Category category = new Category();
        category.setParentCategory(parentCategory);
        MultilingualString multilingualString = new MultilingualString();
        multilingualString.setMap(request.getCategoryName());
        category.setCategoryName(multilingualString);
        category.setId(id);
        category = categoryRepository.save(category);
        return ResponseEntity.ok(CategoryAdminDto.toDto(category));
    }


    @Override
    public ResponseEntity<?> getByIdUser(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        return ResponseEntity.ok(CategoryDtoUser.toDto(category));
    }

    @Override
    public ResponseEntity<?> getByIdAdmin(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        return ResponseEntity.ok(CategoryAdminDto.toDto(category));
    }

    @Override
    public ResponseEntity<?> getAllUser(Pageable pageable) {
        return ResponseEntity.ok(categoryRepository.findAll(pageable).map(CategoryDtoUser::toDto));
    }


    @Override
    public ResponseEntity<?> getByCategory(Long categoryId) {
        if (categoryId == null) {
            List<CategoryDtoUser> list = categoryRepository.findByParentCategoryAndDeletedFalse(null).stream().map(CategoryDtoUser::toDto).collect(Collectors.toList());
            return ResponseEntity.ok(list);
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        List<CategoryDtoUser> list = categoryRepository.findByParentCategoryAndDeletedFalse(category).stream().map(CategoryDtoUser::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getAllAdmin(Pageable pageable) {
        Page<CategoryAdminDto> page = categoryRepository.findAll(pageable).map(CategoryAdminDto::toDto);
        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity<?> edit(CategoryCreateRequest request, MultipartFile icon, long id) {
        Category parentCategory = null;
        if (request.getSubcategoryId() != null) {
            parentCategory = categoryRepository.findById(request.getSubcategoryId()).orElseThrow(CategoryNotFoundException::new);
        }
        Category category = new Category();
        category.setParentCategory(parentCategory);
        if (icon != null) {
            String url = fileService.uploadFile(icon);
            category.setIconUrl(url);

        }

        MultilingualString multilingualString = new MultilingualString();
        multilingualString.setMap(request.getCategoryName());
        category.setCategoryName(multilingualString);
        category.setId(id);
        category.setId(id);
        category = categoryRepository.save(category);
        return ResponseEntity.ok(CategoryAdminDto.toDto(category));
    }


}
