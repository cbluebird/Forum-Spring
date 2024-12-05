package org.jh.forum.user.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jh.forum.user.constant.UserStatus;
import org.jh.forum.user.constant.UserType;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private UserType type;
    private UserStatus status;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private LocalDateTime deletedOn;
}
