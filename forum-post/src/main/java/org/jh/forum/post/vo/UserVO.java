package org.jh.forum.post.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long userId;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
}
