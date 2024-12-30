package org.jh.forum.post.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer type;
    private Integer status;
}