package org.jh.forum.user.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    ORDINARY(1, "普通用户"),
    ADMIN(2, "管理员"),
    OFFICIAL(3, "官方认证");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String name;
}