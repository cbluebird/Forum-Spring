package org.jh.forum.post.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Visibility {
    PRIVATE(1, "私密"),
    FOLLOWERS(2, "关注可见"),
    FRIENDS(3, "好友可见"),
    PUBLIC(4, "公开");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String name;
}