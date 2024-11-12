package org.sunbong.allmart_api.qrcode.domain;

public enum QrCodeType {

    QR_CODE_SIGNUP_DIRECTORY("C:/qrcodes/signup/"),
    QR_CODE_ORDER_DIRECTORY("C:/qrcodes/order/"),
    QR_CODE_PAYMENT_DIRECTORY ("C:/qrcodes/payment/");

    private final String directoryPath;

    QrCodeType(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

}
