package org.sunbong.allmart_api.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QrService {

    // QR 코드 저장 경로
    private static final String UPLOAD_DIR = "C:/upload/";

    public String generateQRCodeForCustomURL(String customUrl) {
        try {
            log.info("QR 코드 생성 시작: URL = {}", customUrl);

            // QR 코드 설정
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            log.info("QR 코드 설정 완료");

            // 고유한 파일명 생성
            String fileName = UUID.randomUUID().toString() + ".png";
            String filePath = UPLOAD_DIR + fileName;
            log.info("QR 코드 파일명 생성: {}", fileName);

            // QR 코드 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(customUrl, BarcodeFormat.QR_CODE, 300, 300, hints);
            log.info("QR 코드 비트매트릭스 생성 완료");

            // 디렉토리 생성 (존재하지 않으면 생성)
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                boolean dirCreated = directory.mkdirs();
                if (dirCreated) {
                    log.info("QR 코드 저장 디렉토리 생성 완료: {}", UPLOAD_DIR);
                } else {
                    log.error("QR 코드 저장 디렉토리 생성 실패: {}", UPLOAD_DIR);
                    throw new RuntimeException("디렉토리 생성 실패");
                }
            } else {
                log.info("QR 코드 저장 디렉토리가 이미 존재합니다: {}", UPLOAD_DIR);
            }

            // QR 코드 이미지 파일 저장
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            log.info("QR 코드 파일 저장 성공: {}", filePath);

            return fileName; // 생성된 파일 URL 반환
        } catch (Exception e) {
            log.error("QR 코드 생성 중 오류 발생", e);
            return null; // 실패 시 null 반환
        }
    }
}
