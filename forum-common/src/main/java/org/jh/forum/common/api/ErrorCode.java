package org.jh.forum.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "成功"),

    SYSTEM_ERROR(10000, "系统异常"),
    FEIGN_ERROR(10100, "Feign调用异常"),
    CRON_ERROR(10200, "定时任务异常"),

    BIZ_ERROR(20000, "业务异常"),
    COMMON_ERROR(20100, "通用异常"),
    COMMON_PARAM_ERROR(20101, "参数错误"),
    GATEWAY_SERV_ERROR(20200, "GATEWAY_SERV异常"),
    AUTH_SERV_ERROR(20300, "AUTH_SERV异常"),
    AUTH_PASSWORD_ERROR(20301, "密码错误"),
    USER_SERV_ERROR(20400, "USER_SERV异常"),
    USERNAME_ALREADY_EXISTS(20401, "该用户名已存在"),
    PHONE_ALREADY_BIND(20402, "该手机号已绑定"),
    EMAIL_ALREADY_BIND(20403, "该邮箱已绑定"),
    USER_NOT_FOUND(20404, "用户不存在"),
    POST_SERV_ERROR(20500, "POST_SERV异常"),
    POST_NOT_FOUND(20501, "帖子不存在"),
    POST_DELETE_FAILED(20502, "删除帖子失败"),
    POST_UPDATE_FAILED(20503, "更新帖子失败"),
    NOTICE_SERV_ERROR(20600, "NOTICE_SERV异常"),
    OSS_SERV_ERROR(20700, "OSS_SERV异常"),
    OSS_UPLOAD_ERROR(20701, "上传失败"),
    SEARCH_SERV_ERROR(20800, "SEARCH_SERV异常"),
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