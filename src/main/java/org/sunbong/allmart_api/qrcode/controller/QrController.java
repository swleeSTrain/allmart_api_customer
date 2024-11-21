package org.sunbong.allmart_api.qrcode.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.allmart_api.qrcode.domain.QrCodeType;
import org.sunbong.allmart_api.qrcode.dto.QrRequestDto;
import org.sunbong.allmart_api.qrcode.dto.QrResponseDto;
import org.sunbong.allmart_api.qrcode.dto.QrSingUpResponseDTO;
import org.sunbong.allmart_api.qrcode.service.QrSerivce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/qrcode")
@Log4j2
public class QrController {

    private final QrSerivce qrService;

    public QrController(QrSerivce qrService) {
        this.qrService = qrService;
    }

    @GetMapping("/signUp")
    public ResponseEntity<QrSingUpResponseDTO> generateSinginQRCode(
            @RequestParam(name = "phoneNumber" ) String phoneNumber, @RequestParam(name = "customerID") Long customerId
    ) {
        QrRequestDto requestDto = QrRequestDto.builder()
                .phoneNumber(phoneNumber)
                .customerID(customerId)
                .build();

        log.info("qr저장 준비");
        List<String> qrList = qrService.generateQRCode(requestDto, QrCodeType.QR_CODE_SIGNUP_DIRECTORY);
        String filePath = qrList.get(0);
        String qrCodeUrl = qrList.get(1);

        log.info("qr저장 완료");
        QrSingUpResponseDTO qrSingUpResponseDTO = QrSingUpResponseDTO.builder()
                .filePath(filePath)
                .qrCodeUrl(qrCodeUrl)
                .build();

        return ResponseEntity.ok(qrSingUpResponseDTO);
    }

    @GetMapping("/order")
    public ResponseEntity<QrSingUpResponseDTO> generateOrderQRCode(
            @RequestParam String data
    ) {
        QrRequestDto requestDto = QrRequestDto.builder().phoneNumber(data).build();
        List<String> resultList = qrService.generateQRCode(requestDto, QrCodeType.QR_CODE_ORDER_DIRECTORY);
        String filePath = resultList.get(0);
        String qrCodeUrl = resultList.get(1);
        QrSingUpResponseDTO qrSingUpResponseDTO = QrSingUpResponseDTO.builder()
                .filePath(filePath)
                .qrCodeUrl(qrCodeUrl)
                .build();
        return ResponseEntity.ok(qrSingUpResponseDTO);
    }

    @GetMapping("/payment")
    public ResponseEntity<QrSingUpResponseDTO> getPaymentQRCode(@RequestParam String data){
        QrRequestDto requestDto = QrRequestDto.builder().phoneNumber(data).build();
        List<String> qrList = qrService.generateQRCode(requestDto, QrCodeType.QR_CODE_PAYMENT_DIRECTORY);
        String filePath = qrList.get(0);
        String qrCodeUrl = qrList.get(1);
        QrSingUpResponseDTO qrSingUpResponseDTO = QrSingUpResponseDTO.builder()
                .filePath(filePath)
                .qrCodeUrl(qrCodeUrl)
                .build();
        return ResponseEntity.ok(qrSingUpResponseDTO);

    }

    @GetMapping("/signUp/verify/{token}")
    public ResponseEntity<String> verifySignUpQRCode(@RequestParam("token") String data){
        String result = qrService.verifySignUpQRCode(data);

        return ResponseEntity.status(303).header("Location", "/api/v1/qrcode/signUp")
                .build();
    }



        @GetMapping("/qr-result")
        public ResponseEntity<Map<String, String>> getQrResult() {
            Map<String, String> response = new HashMap<>();
            response.put("img", "https://example.com/qr-code.png");
            response.put("msg", "QR 인증이 완료되었습니다.");
            return ResponseEntity.ok(response);
        }

}