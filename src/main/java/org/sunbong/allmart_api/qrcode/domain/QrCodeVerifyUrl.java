package org.sunbong.allmart_api.qrcode.domain;

public enum QrCodeVerifyUrl {

    QR_CODE_SIGNUP_VERIFY_URL ("http://10.10.10.192:8080/api/v1/qrcode/signup/verify?phoneNumber="),
    QR_CODE_ORDER_VERIFY_URL ("http://10.10.10.192:8080/api/v1/qrcode/order/verify?data="),
    QR_CODE_PAYMENT_VERIFY_URL ("http://10.10.10.192:8080/api/v1/qrcode/payment/verify?data=");

    private final String URL;

    QrCodeVerifyUrl(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }
}
