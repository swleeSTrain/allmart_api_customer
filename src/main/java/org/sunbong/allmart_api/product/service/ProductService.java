package org.sunbong.allmart_api.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.repository.CategoryRepository;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.common.util.CustomFileUtil;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.dto.ProductAddDTO;
import org.sunbong.allmart_api.product.dto.ProductEditDTO;
import org.sunbong.allmart_api.product.dto.ProductListDTO;
import org.sunbong.allmart_api.product.dto.ProductReadDTO;
import org.sunbong.allmart_api.product.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomFileUtil fileUtil;

    // 조회
    public ProductReadDTO readById(Long id) {

        ProductReadDTO result = productRepository.readById(id);

        return result;
    }

    // 리스트
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {

        PageResponseDTO<ProductListDTO> result = productRepository.list(pageRequestDTO);

        return result;
    }

    // 등록
    public Long register(ProductAddDTO dto) throws Exception {

        // 카테고리 ID로 카테고리 찾기
        Category category = categoryRepository.findById(dto.getCategoryID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        // Product 객체 생성
        Product product = Product.builder()
                .name(dto.getName())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .category(category)  // 카테고리 설정
                .build();

        // 업로드할 파일이 있을 경우
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            List<String> savedFileNames = fileUtil.saveFiles(dto.getFiles());
            savedFileNames.forEach(product::addFile);
        }

        // Product 저장
        Product savedProduct = productRepository.save(product);

        return savedProduct.getProductID();
    }


    // 삭제
    public Long delete(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + id));

        Long productID = product.getProductID();
        productRepository.deleteById(productID);

        return productID;
    }

    // 수정
    public Long edit(Long id, ProductEditDTO dto) throws Exception {

        // 기존 상품 조회
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        if (dto.getCategoryID() != null) {
            // 카테고리 조회 및 적용
            Category newCategory = categoryRepository.findById(dto.getCategoryID())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            existingProduct = existingProduct.toBuilder().category(newCategory).build();
        }

        // 기존 파일 초기화
        existingProduct.clearFiles();

        // 새로운 파일 저장
        List<String> filesToAdd = fileUtil.saveFiles(dto.getFiles());

        filesToAdd.forEach(existingProduct::addFile);

        // 상품 정보 업데이트
        Product updatedProduct = existingProduct.toBuilder()
                .name(dto.getName() != null ? dto.getName() : existingProduct.getName())
                .sku(dto.getSku() != null ? dto.getSku() : existingProduct.getSku())
                .price(dto.getPrice() != null ? dto.getPrice() : existingProduct.getPrice())
                .build();

        // 업데이트된 상품 저장
        productRepository.save(updatedProduct);

        return updatedProduct.getProductID();
    }



}
