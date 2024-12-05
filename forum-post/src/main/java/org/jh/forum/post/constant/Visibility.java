package org.jh.forum.post.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Visibility {
    PRIVATE(0, "私密"),
    FOLLOWERS(1, "关注可见"),
    FRIENDS(2, "好友可见"),
    PUBLIC(3, "公开");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String name;
}