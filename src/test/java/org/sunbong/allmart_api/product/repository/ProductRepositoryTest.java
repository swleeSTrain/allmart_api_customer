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

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
@SpringBootTest
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomFileUtil fileUtil;

    @Test
    public void testProductList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<ProductListDTO> result = productRepository.productList(pageRequestDTO);

        // DTO 리스트 출력
        result.getDtoList().forEach(dto -> {
            log.info("Product DTO: " + dto);
        });
    }

    @Test
    public void testProductSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .keyword("피카츄")
                .type("name&sku")
                .build();

        PageResponseDTO<ProductListDTO> result = productRepository.productList(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info("Search Result DTO: " + dto));
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

        List<String> savedFileNames = fileUtil.saveFiles(List.of(mockFile1));

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


    }
}
