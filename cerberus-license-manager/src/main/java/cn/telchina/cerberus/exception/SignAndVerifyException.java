package cn.telchina.cerberus.exception;

public class SignAndVerifyException extends RuntimeException {
    public SignAndVerifyException(String message) {
        super(message);
    }

    public SignAndVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
