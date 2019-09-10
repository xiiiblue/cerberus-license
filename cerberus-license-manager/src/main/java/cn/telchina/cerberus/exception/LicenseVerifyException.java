package cn.telchina.cerberus.exception;

public class LicenseVerifyException extends RuntimeException {
    public LicenseVerifyException(String message) {
        super(message);
    }

    public LicenseVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
