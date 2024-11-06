package org.sunbong.allmart_api.product.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.common.util.CustomFileUtil;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.dto.ProductListDTO;
import org.sunbong.allmart_api.product.dto.ProductReadDTO;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomFileUtil fileUtil;

    @Test
    public void testRead() {

        long productID = 101L;

        ProductReadDTO productReadDTO = productRepository.readById(productID);

        log.info("ProductReadDTO: " + productReadDTO);

        // 검증
        assertNotNull(productReadDTO, "ProductReadDTO should not be null");
        assertEquals(101L, productReadDTO.getProductID(), "Product ID should match");
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
    @Transactional
    @Rollback(false)
    public void testDelete() {
        Long productID = 101L;
        productRepository.deleteById(productID);

        boolean exists = productRepository.findById(productID).isPresent();

        assertTrue(!exists, "Product should be deleted");
    }

    @Test
    @Transactional
    @Rollback(false) // 롤백 방지
    public void testUpdate() throws Exception {

        log.info("Test Update Product");

        long productID = 101L;

        Product initialProduct = productRepository.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 수정할 파일 준비 (가짜 MultipartFile 생성)
        Path sampleImagePath = Paths.get("src/test/resources/sample.jpg");

        MockMultipartFile mockFile1 = new MockMultipartFile(
                "file",
                "updatedImage1.jpg",
                "image/jpeg",
                new FileInputStream(sampleImagePath.toFile())
        );

        MockMultipartFile mockFile2 = new MockMultipartFile(
                "file",
                "updatedImage2.jpg",
                "image/jpeg",
                new FileInputStream(sampleImagePath.toFile())
        );

        List<String> savedFileNames = fileUtil.saveFiles(List.of(mockFile1, mockFile2));

        // 상품 수정 DTO 생성
        ProductEditDTO productEditDTO = ProductEditDTO.builder()
                .name("수정된 상품명")
                .sku("NEW-SKU")
                .price(BigDecimal.valueOf(3000))
                .files(List.of(mockFile1, mockFile2))
                .build();

        // 기존 상품을 수정된 정보로 업데이트
        Product updatedProduct = productEditDTO.toUpdatedProduct(initialProduct);

        // 저장된 파일 이름들을 추가 (중복 제거)
        savedFileNames.forEach(savedFileName -> {
            boolean exists = updatedProduct.getAttachFiles().stream()
                    .anyMatch(f -> f.getImageURL().equals(savedFileName));
            if (!exists) {
                updatedProduct.addFile(savedFileName);
            }
        });

        // 업데이트된 상품 저장
        productRepository.save(updatedProduct);

        // 저장된 상품을 확인하고 검증
        Product savedProduct = productRepository.findById(updatedProduct.getProductID()).orElseThrow();
        assertNotNull(savedProduct, "Product should be saved successfully");
        assertEquals("수정된 상품명", savedProduct.getName(), "Product name should match");
        assertEquals("NEW-SKU", savedProduct.getSku(), "Product SKU should match");
        assertEquals(BigDecimal.valueOf(3000), savedProduct.getPrice(), "Product price should match");

    }


    @Test
    @Transactional
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

        for (int i = 0; i < 100; i++) {

            List<String> savedFileNames = fileUtil.saveFiles(List.of(mockFile1));

            // Product 엔티티 생성 및 파일명 추가
            Product product = Product.builder()
                    .name("테스트 상품 " + (i + 1))
                    .sku("APL-TEST-1KG")
                    .price(BigDecimal.valueOf(3000))
                    .build();

            // 파일명을 Product에 추가
            savedFileNames.forEach(product::addFile);

            // Product 저장
            productRepository.save(product);
        }

        // 검증
        assertTrue(productRepository.count() > 0, "Product count should be greater than 0 after registration");
    }

    // 한 개만 추가
    @Test
    @Transactional
    @Rollback(false) // 롤백 방지
    public void testOneRegister() throws Exception {

        log.info("Test One Register");

        // 임의 파일 경로 설정
        Path sampleImagePath = Paths.get("src/test/resources/sample.jpg");

        // 가짜 MultipartFile 생성
        MockMultipartFile mockFile1 = new MockMultipartFile(
                "file",
                "sample1.jpg",
                "image/jpeg",
                new FileInputStream(sampleImagePath.toFile())
        );

        // 가짜 MultipartFile 생성
        MockMultipartFile mockFile2 = new MockMultipartFile(
                "file",
                "sample1.jpg",
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
        productRepository.save(product);

        // 검증
        Product savedProduct = productRepository.findById(product.getProductID()).orElse(null);
        assertNotNull(savedProduct, "Product should be saved successfully");
        assertEquals("피카츄", savedProduct.getName(), "Product name should match");
        assertTrue(savedProduct.getAttachFiles().size() > 0, "Product should have attached files");
    }
}
