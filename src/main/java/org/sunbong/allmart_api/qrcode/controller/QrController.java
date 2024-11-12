package org.sunbong.allmart_api.qrcode.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.allmart_api.qrcode.domain.QrCodeType;
import org.sunbong.allmart_api.qrcode.dto.QrRequestDto;
import org.sunbong.allmart_api.qrcode.service.QrSerivce;


@RestController
@RequestMapping("/api/qrcode")
public class QrController {


    private final QrSerivce qrService;

    public QrController(QrSerivce qrService) {
        this.qrService = qrService;
    }

    @GetMapping("/signUp")
    public ResponseEntity<String> generateSinginQRCode(
            @RequestParam String data
    ) {
        QrRequestDto requestDto = QrRequestDto.builder().data(data).build();
        String filePath = qrService.generateQRCode(requestDto, QrCodeType.QR_CODE_SIGNUP_DIRECTORY);
        return ResponseEntity.ok("QR 코드가 생성되었습니다: " + filePath);
    }

    @GetMapping("/order")
    public ResponseEntity<String> generateOrderQRCode(
            @RequestParam String data
    ) {
        QrRequestDto requestDto = QrRequestDto.builder().data(data).build();
        String filePath = qrService.generateQRCode(requestDto, QrCodeType.QR_CODE_ORDER_DIRECTORY);
        return ResponseEntity.ok("QR 코드가 생성되었습니다: " + filePath);
    }

    @GetMapping("/payment")
    public ResponseEntity<String> getPaymentQRCode(@RequestParam String data){
        QrRequestDto requestDto = QrRequestDto.builder().data(data).build();
        String filePath = qrService.generateQRCode(requestDto, QrCodeType.QR_CODE_PAYMENT_DIRECTORY);
        return ResponseEntity.ok("QR 코드가 생성되었습니다: " + filePath);

    }

    @GetMapping("/signUp/verify")
    public ResponseEntity<String> verifySignUpQRCode(@RequestParam String data){

        return null;
    }

}