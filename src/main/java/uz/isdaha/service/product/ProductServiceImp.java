package uz.isdaha.service.product;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.isdaha.entity.Category;
import uz.isdaha.entity.FavoriteProduct;
import uz.isdaha.entity.Product;
import uz.isdaha.entity.User;
import uz.isdaha.entity.lang.MultilingualString;
import uz.isdaha.exception.CategoryNotFoundException;
import uz.isdaha.exception.ProductNotFoundException;
import uz.isdaha.payload.request.ProductPageableRequest;
import uz.isdaha.payload.request.ProductRequest;
import uz.isdaha.payload.response.ProductAdminResponse;
import uz.isdaha.payload.response.ProductResponse;
import uz.isdaha.repository.CategoryRepository;
import uz.isdaha.repository.FavouriteProductRepository;
import uz.isdaha.repository.ProductRepository;
import uz.isdaha.service.FileService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    private final FileService fileService;

    private final CategoryRepository categoryRepository;

    private final FavouriteProductRepository favouriteProductRepository;

    @Override
    public ResponseEntity<?> get(Long id) {

        final Product product = productRepository.findByIdAndAvailableTrue(id).orElseThrow(ProductNotFoundException::new);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(ProductResponse.toDto(product, isFavourite(product, user)));

        } catch (Exception e) {
            return ResponseEntity.ok(ProductResponse.toDto(product, false));

        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setAvailable(!product.isAvailable());
        productRepository.save(product);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> update(Long id, ProductRequest productRequest, MultipartFile image) {
        Product product1 = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (product1.getImageUrl() != null) {
            fileService.delete(product1.getImageUrl());
        }

        String url = null;
        if (image != null) {
            url = fileService.uploadFile(image);
        }

        Product product = Product.builder()
            .category(getCategoryById(productRequest.getCategoryId()))
            .imageUrl(url)
            .available(true)
            .readyTime(productRequest.getReadyTime())
            .price(BigDecimal.valueOf(productRequest.getPrice()))
            .discount(productRequest.getDiscount())
            .build();
        MultilingualString description = new MultilingualString();
        description.setMap(productRequest.getDescription());
        product.setDescription(description);
        description = new MultilingualString();
        description.setMap(productRequest.getName());
        product.setProductName(description);
        product.setId(id);
        return ResponseEntity.ok(ProductAdminResponse.toDTO(productRepository.save(product)));
    }

    @Override
    public ResponseEntity<?> getProductsByCategory(long categoryId) {
        final Category category = categoryRepository.findById(categoryId).orElseThrow(ProductNotFoundException::new);
        final List<Product> products = productRepository.findByCategoryAndAvailableTrue(category);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = (User) authentication.getPrincipal();
            final List<ProductResponse> productResponses = products.stream().map((Product product) -> ProductResponse.toDto(product, isFavourite(product, user))).collect(Collectors.toList());
            return ResponseEntity.ok(productResponses);
        } catch (Exception e) {
            final List<ProductResponse> productResponses = products.stream().map((Product product) -> ProductResponse.toDto(product, false)).collect(Collectors.toList());
            return ResponseEntity.ok(productResponses);
        }
    }

    @Override
    public ResponseEntity<?> add(ProductRequest request, MultipartFile image) {
        String url = "";
        if (image != null) {
            url = fileService.uploadFile(image);
        }

        Product product = Product.builder()
            .category(getCategoryById(request.getCategoryId()))
            .imageUrl(url)
            .available(true)
            .readyTime(request.getReadyTime())
            .price(BigDecimal.valueOf(request.getPrice()))
            .discount(request.getDiscount())
            .code(request.getCode())
            .build();

        MultilingualString description = new MultilingualString();
        description.setMap(request.getDescription());
        product.setDescription(description);
        description = new MultilingualString();
        description.setMap(request.getName());
        product.setProductName(description);
        return ResponseEntity.ok(ProductAdminResponse.toDTO(productRepository.save(product)));
    }

    @Override
    public ResponseEntity<?> getAllForAdmin(Pageable pageable) {
        final Page<ProductAdminResponse> page = productRepository.findAll(pageable).map(
            ProductAdminResponse::toDTO
        );
        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity<?> getAllByPage(Pageable pageable) {
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "category"));
        return ResponseEntity.ok(productRepository.findAllByAvailable(true, page).stream().map((Product product) -> ProductResponse.toDto(product, false)).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> addFavouriteProduct(long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        FavoriteProduct favoriteProduct = favouriteProductRepository.findByProduct(product).orElse(null);
        if (favoriteProduct != null) {
            favoriteProduct.setFavourite(!favoriteProduct.isFavourite());

        } else {
            favoriteProduct = favouriteProductRepository.save(new FavoriteProduct(product, user, true));
        }
        favouriteProductRepository.save(favoriteProduct);
        return ResponseEntity.ok(ProductResponse.toDto(product, isFavourite(product, user)));
    }

    @Override
    public ResponseEntity<?> getAllFavourites() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(favouriteProductRepository
            .findByUserAndFavouriteTrue(user).stream()
            .map(favoriteProduct ->
                ProductResponse.toDto(favoriteProduct.getProduct(), favoriteProduct.isFavourite()))
            .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getByIdAdmin(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ResponseEntity.ok(ProductAdminResponse.toDTO(product));
    }

    @Override
    public ResponseEntity<?> getAllByProperties(ProductPageableRequest pageableRequest) {
        Pageable pageable = pageRequestGenerator(pageableRequest);
        final Page<Product> page = productRepository.findAllByAvailable(pageableRequest.getAvailable(), pageable);
        final Page<ProductAdminResponse> responses = page.map(ProductAdminResponse::toDTO);
        return ResponseEntity.ok(responses);
    }

    private Pageable pageRequestGenerator(ProductPageableRequest pageableRequest) {
        PageRequest request = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize());

        if (pageableRequest.getSortBy() != null && pageableRequest.getSortBy().length != 0) {
            if (pageableRequest.getDirection() != null) {
                request.withSort(
                    Sort.Direction.valueOf(pageableRequest.getDirection()),
                    pageableRequest.getSortBy()
                );
            }
        }
        return request;
    }


    private boolean isFavourite(Product product, User user) {
        if (favouriteProductRepository.findByUserAndProduct(user, product).isPresent()) {
            return favouriteProductRepository.findByUserAndProduct(user, product).get().isFavourite();
        }
        return false;

    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }
}
