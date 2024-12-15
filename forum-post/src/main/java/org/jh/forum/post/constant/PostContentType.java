package org.jh.forum.post.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostContentType {
    TEXT(1, "文字段落"),
    IMAGE(2, "图片地址"),
    VIDEO(3, "视频地址"),
    LINK(4, "链接地址");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String name;
}