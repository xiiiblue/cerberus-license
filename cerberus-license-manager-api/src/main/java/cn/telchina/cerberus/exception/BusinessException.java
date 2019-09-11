package cn.telchina.cerberus.exception;

public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
