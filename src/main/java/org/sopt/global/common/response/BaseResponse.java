package org.sopt.global.common.response;

public interface BaseResponse {
    boolean success();
    String code();
    String message();
}
