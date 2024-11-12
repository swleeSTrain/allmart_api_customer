package org.sunbong.allmart_api.qrcode.domain;

public enum QrCodeVerifyUrl {

    QR_CODE_SIGNUP_VERIFY_URL ("http://10.10.10.24:8080/qr/signUp/verify?data="),
    QR_CODE_ORDER_VERIFY_URL ("http://10.10.10.24:8080/qr/order/verify?data="),
    QR_CODE_PAYMENT_VERIFY_URL ("http://10.10.10.24:8080/qr/payment/verify?data=");

    private final String URL;

    QrCodeVerifyUrl(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }
}
