package org.sopt.common.exception;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
    HttpStatus getStatus();
    String getMessage();
    String getCode();
}
