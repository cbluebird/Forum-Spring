package org.jh.forum.user.vo;

import lombok.Data;
import org.jh.forum.user.constant.UserStatus;
import org.jh.forum.user.constant.UserType;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private UserType type;
    private UserStatus status;
}