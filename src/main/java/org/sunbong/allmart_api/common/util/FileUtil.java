package org.sunbong.allmart_api.common.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Log4j2
@Component
public class FileUtil {

    @Value("${org.sunbong.upload.path}")
    private String uploadDir;

    // 파일 저장 메서드를 public으로 변경
    public String saveFile(MultipartFile file) throws IOException {
        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            return fileName;
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            throw new IOException("파일 저장 실패", e);
        }
    }

    public void deleteFile(String fileName) throws IOException {

        Path filePath = Paths.get(uploadDir, fileName); // 파일 경로 생성

        try {

            Files.deleteIfExists(filePath); // 파일이 존재하면 삭제
            log.info("파일 삭제 성공: " + fileName);

        } catch (IOException e) {

            log.error("파일 삭제 중 오류 발생: " + fileName, e);
            throw new IOException("파일 삭제 실패", e); // 예외 처리

        }
    }
}
