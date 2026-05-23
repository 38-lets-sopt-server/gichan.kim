package org.sopt.global.common.code;

import org.springframework.http.HttpStatus;

public interface BaseCode {
    HttpStatus getStatus();
    String getMessage();
    String getCode();
}
