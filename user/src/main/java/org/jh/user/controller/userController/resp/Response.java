package org.jh.user.controller.userController.resp;

import lombok.Data;

@Data
public class Response {
    private Integer code;
    private Object data;
    private String msg;

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}