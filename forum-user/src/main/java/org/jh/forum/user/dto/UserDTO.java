package org.jh.forum.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.jh.forum.user.constant.UserStatus;
import org.jh.forum.user.constant.UserType;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "用户名不能为空", groups = {Register.class})
    private String username;
    @NotBlank(message = "密码不能为空", groups = {Register.class})
    private String password;
    @NotBlank(message = "昵称不能为空", groups = {Register.class})
    private String nickname;
    @NotBlank(message = "手机号不能为空", groups = {Register.class})
    private String phone;
    @NotBlank(message = "邮箱不能为空", groups = {Register.class})
    private String email;
    private String avatar;
    private UserType type;
    private UserStatus status;

    public interface Register {
    }
}
