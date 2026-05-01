package org.sopt.common.response;

import java.time.LocalDateTime;

public interface BaseResponse {
    boolean success();
    String code();
    String message();
}
