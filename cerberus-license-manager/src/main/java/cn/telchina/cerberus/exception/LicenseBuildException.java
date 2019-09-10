package cn.telchina.cerberus.exception;

public class LicenseBuildException extends RuntimeException {
    public LicenseBuildException(String message) {
        super(message);
    }

    public LicenseBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
