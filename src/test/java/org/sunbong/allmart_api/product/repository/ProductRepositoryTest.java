package org.sunbong.allmart_api.product.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.domain.CategoryProduct;
import org.sunbong.allmart_api.category.repository.CategoryProductRepository;
import org.sunbong.allmart_api.category.repository.CategoryRepository;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.common.util.CustomFileUtil;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.dto.ProductEditDTO;
import org.sunbong.allmart_api.product.dto.ProductListDTO;
import org.sunbong.allmart_api.product.dto.ProductReadDTO;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private CustomFileUtil fileUtil;

    @Test
    public void testRead() {

        long productID = 1L;

        ProductReadDTO productReadDTO = productRepository.readById(productID);

        log.info("ProductReadDTO: " + productReadDTO);

        // 검증
        assertNotNull(productReadDTO, "ProductReadDTO should not be null");
        assertEquals(1L, productReadDTO.getProductID(), "Product ID should match");
        assertNotNull(productReadDTO.getName(), "Product name should not be null");
        assertNotNull(productReadDTO.getPrice(), "Product price should not be null");
        assertTrue(productReadDTO.getAttachFiles().size() > 0, "Attach files should be present");
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<ProductListDTO> result = productRepository.list(pageRequestDTO);

        // DTO 리스트 출력
        result.getDtoList().forEach(dto -> {
            log.info("Product DTO: " + dto);
        });

        // 검증
        assertNotNull(result, "Result should not be null");
        assertFalse(result.getDtoList().isEmpty(), "Product list should not be empty");
        assertTrue(result.getTotalCount() > 0, "Total count should be greater than 0");
    }

    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .keyword("수정")
                .type("name&sku")
                .build();

        PageResponseDTO<ProductListDTO> result = productRepository.list(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info("Search Result DTO: " + dto));

        // 검증
        assertNotNull(result, "Search result should not be null");
        assertFalse(result.getDtoList().isEmpty(), "Search results should not be empty");
    }

    @Test
    @Rollback(false)
    public void testDelete() {

        Long productID = 101L;

        // Product와 연결된 CategoryProduct 엔티티를 먼저 삭제
        List<CategoryProduct> categoryProducts = categoryProductRepository.findByProductProductID(productID);
        categoryProductRepository.deleteAll(categoryProducts);

        // Product 삭제
        productRepository.deleteById(productID);

        boolean exists = productRepository.findById(productID).isPresent();

        assertTrue(!exists, "Product should be deleted");
    }

    @Test
    @Rollback(false)
    public void testEdit() throws Exception {
        log.info("Test Edit Product");

        long productID = 1L;
        Product initialProduct = productRepository.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Path sampleImagePath = Paths.get("src/test/resources/sample.jpg");
        MockMultipartFile mockFile1 = new MockMultipartFile("file", "updatedImage1.jpg", "image/jpeg", new FileInputStream(sampleImagePath.toFile()));
        MockMultipartFile mockFile2 = new MockMultipartFile("file", "updatedImage2.jpg", "image/jpeg", new FileInputStream(sampleImagePath.toFile()));
        List<String> savedFileNames = fileUtil.saveFiles(List.of(mockFile1, mockFile2));

        ProductEditDTO productEditDTO = ProductEditDTO.builder()
                .name("포켓몬")
                .sku("NEW-SKU2")
                .price(BigDecimal.valueOf(3000))
                .files(List.of(mockFile1, mockFile2))
                .categoryName("포켓몬")
                .build();

        Product updatedProduct = productEditDTO.toUpdatedProduct(initialProduct);

        savedFileNames.forEach(savedFileName -> {
            if (updatedProduct.getAttachFiles().stream().noneMatch(f -> f.getImageURL().equals(savedFileName))) {
                updatedProduct.addFile(savedFileName);
            }
        });

        // 카테고리 업데이트 처리 (기존 CategoryProduct 업데이트)
        if (productEditDTO.getCategoryName() != null) {
            Category newCategory = categoryRepository.findByName(productEditDTO.getCategoryName())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

            // 현재 Product에 해당하는 CategoryProduct를 가져옴
            List<CategoryProduct> categoryProducts = categoryProductRepository.findByProductProductID(updatedProduct.getProductID());

            if (!categoryProducts.isEmpty()) {
                // 기존 CategoryProduct의 카테고리를 새로운 카테고리로 업데이트
                CategoryProduct categoryProduct = categoryProducts.get(0);  // 여러 개 중 첫 번째 것만 변경하는 경우
                categoryProduct = categoryProduct.toBuilder().category(newCategory).build();
                categoryProductRepository.save(categoryProduct);
            } else {
                // 기존 매핑이 없는 경우 새로운 CategoryProduct 생성
                CategoryProduct categoryProduct = CategoryProduct.builder()
                        .product(updatedProduct)
                        .category(newCategory)
                        .build();
                categoryProductRepository.save(categoryProduct);
            }
        }


        productRepository.save(updatedProduct);

        Product savedProduct = productRepository.findById(updatedProduct.getProductID()).orElseThrow();
        assertNotNull(savedProduct, "Product should be saved successfully");
        assertEquals("포켓몬", savedProduct.getName(), "Product name should match");
        assertEquals("NEW-SKU2", savedProduct.getSku(), "Product SKU should match");
        assertEquals(BigDecimal.valueOf(3000), savedProduct.getPrice(), "Product price should match");
    }



    @Test
    @Rollback(false) // 롤백 방지
    public void testRegister() throws Exception {

        log.info("Test Register");

        // 임의 파일 경로 설정
        Path sampleImagePath = Paths.get("src/test/resources/sample.jpg");

        // 가짜 MultipartFile 생성
        MockMultipartFile mockFile1 = new MockMultipartFile(
                "file",
                "sample1.jpg",
                "image/jpeg",
                new FileInputStream(sampleImagePath.toFile())
        );

        // 카테고리 검색 또는 예외 처리
        String categoryName = "식료품";
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        for (int i = 0; i < 100; i++) {

            List<String> savedFileNames = fileUtil.saveFiles(List.of(mockFile1));

            // Product 엔티티 생성 및 파일명 추가
            Product product = Product.builder()
                    .name("테스트 상품 " + (i + 1))
                    .sku("APL-TEST-1KG " + (i + 1))
                    .price(BigDecimal.valueOf(3000))
                    .build();

            // 파일명을 Product에 추가
            savedFileNames.forEach(product::addFile);

            // Product 저장
            product = productRepository.save(product);

            // CategoryProduct를 생성해 Product와 Category 연결
            CategoryProduct categoryProduct = CategoryProduct.builder()
                    .product(product)
                    .category(category)
                    .build();
            categoryProductRepository.save(categoryProduct);
        }

        // 검증
        assertTrue(productRepository.count() > 0, "Product count should be greater than 0 after registration");
    }

    // 한 개만 추가
    @Test
    @Rollback(false) // 롤백 방지
    public void testOneRegister() throws Exception {

        log.info("Test One Register with Category");

        // 임의 파일 경로 설정
        Path sampleImagePath = Paths.get("src/test/resources/sample.jpg");

        // 가짜 MultipartFile 생성
        MockMultipartFile mockFile1 = new MockMultipartFile(
                "file",
                "sample1.jpg",
                "image/jpeg",
                new FileInputStream(sampleImagePath.toFile())
        );

        MockMultipartFile mockFile2 = new MockMultipartFile(
                "file",
                "sample2.jpg",
                "image/jpeg",
                new FileInputStream(sampleImagePath.toFile())
        );

        List<String> savedFileNames = fileUtil.saveFiles(List.of(mockFile1, mockFile2));

        // Product 엔티티 생성 및 파일명 추가
        Product product = Product.builder()
                .name("피카츄")
                .sku("전기타입")
                .price(BigDecimal.valueOf(3000))
                .build();

        // 파일명을 Product에 추가
        savedFileNames.forEach(product::addFile);

        // Product 저장
        product = productRepository.save(product);

        // 카테고리 검색 및 Product와 연결
        String categoryName = "식료품";
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        // CategoryProduct를 생성해 Product와 Category 연결
        CategoryProduct categoryProduct = CategoryProduct.builder()
                .product(product)
                .category(category)
                .build();
        categoryProductRepository.save(categoryProduct);

        // 검증
        Product savedProduct = productRepository.findById(product.getProductID()).orElse(null);
        assertNotNull(savedProduct, "Product should be saved successfully");
        assertEquals("피카츄", savedProduct.getName(), "Product name should match");
        assertTrue(savedProduct.getAttachFiles().size() > 0, "Product should have attached files");

        // 카테고리 검증
        assertEquals("식료품", categoryProduct.getCategory().getName(), "Product category should be 식료품");
    }

}
