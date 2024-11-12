package org.sunbong.allmart_api.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.allmart_api.common.util.CustomFileUtil;
import org.sunbong.allmart_api.product.dto.ProductAddDTO;
import org.sunbong.allmart_api.product.dto.ProductEditDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Commit
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomFileUtil fileUtil;

    // Mock 파일 생성 메서드
    private MockMultipartFile createMockMultipartFile(String filename) throws IOException {
        Path sampleImagePath = Paths.get("src/test/resources/" + filename);
        return new MockMultipartFile("file", filename, "image/jpeg", new FileInputStream(sampleImagePath.toFile()));
    }

    @Test
    public void testRegisterDummies() throws Exception {
        for (int i = 1; i <= 100; i++) {
            // 테스트용 더미 파일 생성
            MultipartFile mockFile = createMockMultipartFile("sample.jpg");

            // ProductAddDTO 생성 (더미 데이터)
            ProductAddDTO dto = ProductAddDTO.builder()
                    .name("테스트 상품 " + i)
                    .sku("TEST-SKU-" + i)
                    .price(BigDecimal.valueOf(2000 + i)) // 가격은 2000부터 시작해서 i만큼 증가
                    .files(List.of(mockFile)) // 더미 파일 추가
                    .categoryId(1L) // 카테고리 ID 설정 (예: 1L)
                    .build();

            // 상품 등록
            Long productId = productService.register(dto);
            assertNotNull(productId, "Product ID should not be null after registration");

        }
    }


    // 등록 테스트
    @Test
    public void testRegister() throws Exception {
        // 테스트용 파일 생성 및 등록
        MultipartFile mockFile = createMockMultipartFile("sample.jpg");
        ProductAddDTO dto = ProductAddDTO.builder()
                .name("테스트 상품")
                .sku("TEST-SKU")
                .price(BigDecimal.valueOf(2000))
                .files(List.of(mockFile))
                .categoryId(1L) // 올바르게 categoryId 설정
                .build();

        Long productId = productService.register(dto);
        assertNotNull(productId, "Product ID should not be null after registration");
    }


    // 수정 테스트
    @Test
    public void testEdit() throws Exception {

        Long productId = 101L;

        // 새로운 파일을 추가하고 ord 기준으로 파일 삭제
        MultipartFile newMockFile = createMockMultipartFile("change_sample.jpg");
        ProductEditDTO editDto = ProductEditDTO.builder()
                .name("수정된 상품")
                .sku("UPDATED-SKU")
                .price(BigDecimal.valueOf(3500))
                .files(List.of(newMockFile)) // 새 파일 추가
//                .ordsToDelete(List.of(0)) // ord가 0인 파일 삭제
                .categoryID(2L) // 변경할 카테고리 ID 전달
                .build();

        // 제품 수정
        Long editedProductId = productService.edit(productId, editDto);

        // 기존 ID와 동일한지 확인
        assertEquals(productId, editedProductId, "Product ID should remain the same after edit");

    }



}

