package org.sunbong.allmart_api.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.qrcode.domain.QrCode;
import org.sunbong.allmart_api.qrcode.domain.QrCodeType;
import org.sunbong.allmart_api.qrcode.dto.QrRequestDto;
import org.sunbong.allmart_api.qrcode.repository.QrRepository;


import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.sunbong.allmart_api.qrcode.domain.QrCodeVerifyUrl.*;


@Service
public class QrSerivce {

    //만료체크
    @Autowired
    private QrRepository qrRepository;

    //만료시간 설정 값
    private static final int QR_CODE_SIGNUP_EXPIRED = 1;
    private static final int QR_CODE_ORDER_EXPIRED = 1;
    private static final int QR_CODE_PAYMENT_EXPIRED = 1;

    public String generateQRCode(QrRequestDto qrRequestDto, QrCodeType qrCodeType) {

        try {
                //QR 코드 설정
                Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                //고유한 파일명 생성(UUID 사용)
                String fileName = UUID.randomUUID().toString() + ".png";
                String filePath = qrCodeType.getDirectoryPath() + fileName;

                LocalDateTime expireTime;
                String qrCodeUrl;
                String encoded= URLEncoder.encode(qrRequestDto.getData(), StandardCharsets.UTF_8);
                switch (qrCodeType){

                case QR_CODE_SIGNUP_DIRECTORY:
                    expireTime = LocalDateTime.now().plusMonths(QR_CODE_SIGNUP_EXPIRED);
                    qrCodeUrl = QR_CODE_SIGNUP_VERIFY_URL.getURL() + encoded;
                    break;

                case QR_CODE_ORDER_DIRECTORY :
                    expireTime = LocalDateTime.now().plusMonths(QR_CODE_ORDER_EXPIRED);
                    qrCodeUrl =  QR_CODE_ORDER_VERIFY_URL.getURL()+ encoded;
                    break;

                case QR_CODE_PAYMENT_DIRECTORY:
                    expireTime = LocalDateTime.now().plusMonths(QR_CODE_PAYMENT_EXPIRED);
                    qrCodeUrl = QR_CODE_PAYMENT_VERIFY_URL.getURL() + encoded;
                    break;

                default:
                    expireTime = LocalDateTime.now().plusMonths(QR_CODE_SIGNUP_EXPIRED);
                    qrCodeUrl = QR_CODE_SIGNUP_VERIFY_URL.getURL() + encoded;
            }

                //QR 코드 생성
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl,BarcodeFormat.QR_CODE,300, 300, hints);

                //디렉토리 생성(존재하지 않는 경우)
                File directory = new File(qrCodeType.getDirectoryPath());
                if(!directory.exists()){
                    directory.mkdirs();
            }

                Path path = FileSystems.getDefault().getPath(filePath);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);



            QrCode qrCode = QrCode.builder()
                    .fileName(fileName)
                    .createTime(LocalDateTime.now())
                    .data(qrRequestDto.getData())
                    .qrCodeType(qrCodeType)
                    .expireTime(expireTime)
                    .qrCodeURL(qrCodeUrl)
                    .build();

            qrRepository.save(qrCode);
            return filePath;

        }catch(Exception e){
            e.printStackTrace();
            return "QR 코드 생성 중 오류가 발생했습니다.";
        }

    }

    private boolean isExpiredQRCode(LocalDateTime now, String date){
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return now.isAfter(dateTime.plusMinutes(QR_CODE_SIGNUP_EXPIRED) );
    }

}
