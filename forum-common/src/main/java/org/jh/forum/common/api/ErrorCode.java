package org.jh.forum.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "成功"),

    SYSTEM_ERROR(10000, "系统异常"),
    FEIGN_ERROR(10100, "Feign调用异常"),

    BIZ_ERROR(20000, "业务异常"),
    COMMON_ERROR(20100, "通用异常"),
    COMMON_PARAM_ERROR(20101, "参数错误"),
    GATEWAY_SERV_ERROR(20200, "GATEWAY_SERV异常"),
    AUTH_SERV_ERROR(20300, "AUTH_SERV异常"),
    AUTH_PASSWORD_ERROR(20301, "密码错误"),
    USER_SERV_ERROR(20400, "USER_SERV异常"),
    USER_ALREADY_EXISTS(20401, "用户已存在"),
    USER_NOT_FOUND(20402, "用户不存在"),
    POST_SERV_ERROR(20500, "POST_SERV异常"),
    NOTICE_SERV_ERROR(20600, "NOTICE_SERV异常"),
    OSS_SERV_ERROR(20700, "OSS_SERV异常"),
    OSS_UPLOAD_ERROR(20701, "上传失败"),
    ;

    private final Integer code;
    private final String message;

    public static ErrorCode fromCode(Integer code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
}