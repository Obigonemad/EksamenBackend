package dat.security.exceptions;

import dat.utils.Utils;

import java.time.LocalDateTime;

/**
 * Purpose: To handle exceptions in the API

 */
public class ApiException extends RuntimeException {
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
        this.getTimestamp();
    }

    public int getCode() {
        return code;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }
}
