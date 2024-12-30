package org.jh.forum.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(0, "成功"),

    SYSTEM_ERROR(10000, "系统异常"),
    DB_ERROR(10100, "数据库异常"),
    DB_MYSQL_ERROR(10101, "MySQL异常"),
    FEIGN_ERROR(10200, "Feign调用异常"),
    CRON_ERROR(10300, "定时任务异常"),

    BIZ_ERROR(20000, "业务异常"),
    COMMON_ERROR(20100, "通用异常"),
    COMMON_PARAM_ERROR(20101, "参数错误"),
    COMMON_PERMISSION_ERROR(20102, "权限不足"),
    COMMON_DISABLE_EXTERNAL_CALL(20103, "禁止外部调用"),
    GATEWAY_SERV_ERROR(20200, "GATEWAY_SERV异常"),
    AUTH_SERV_ERROR(20300, "AUTH_SERV异常"),
    AUTH_USERNAME_OR_PASSWORD_ERROR(20301, "用户名或密码错误"),
    USER_SERV_ERROR(20400, "USER_SERV异常"),
    USERNAME_ALREADY_EXISTS(20401, "该用户名已存在"),
    PHONE_ALREADY_BIND(20402, "该手机号已绑定"),
    EMAIL_ALREADY_BIND(20403, "该邮箱已绑定"),
    USER_NOT_FOUND(20404, "用户不存在"),
    POST_SERV_ERROR(20500, "POST_SERV异常"),
    POST_CATEGORY_ALREADY_EXIST(20501, "该板块已存在"),
    POST_CATEGORY_NOT_FOUND(20502, "板块不存在"),
    POST_TAG_ALREADY_EXIST(20503, "该标签已存在"),
    POST_TAG_NOT_FOUND(20504, "标签不存在"),
    POST_NOT_FOUND(20505, "帖子不存在"),
    POST_REPLY_NOT_FOUND(20506, "回复不存在"),
    POST_ALREADY_UPVOTE(20507, "已点赞"),
    POST_DOWNVOTE(20508, "未点赞"),
    POST_ALREADY_COLLECT(20509, "已收藏"),
    POST_UNCOLLECT(20510, "未收藏"),
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