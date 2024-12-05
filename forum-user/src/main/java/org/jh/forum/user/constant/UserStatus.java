package org.jh.forum.user.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE(1, "正常"),
    DEACTIVATED(2, "注销"),
    BANNED(3, "禁言");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String name;
}