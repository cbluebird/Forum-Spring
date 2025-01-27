package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("post_collect")
public class Collect {
    @TableId
    private Integer id;
    private Integer postId;
    private Integer userId;
    private LocalDateTime createdOn;
}