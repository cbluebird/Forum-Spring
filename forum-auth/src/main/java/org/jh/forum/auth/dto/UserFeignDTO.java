package org.jh.forum.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFeignDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer type;
    private Integer status;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private LocalDateTime deletedOn;
}