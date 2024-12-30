package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("post_tag")
public class PostTag {
    @TableId
    private Integer id;
    private Integer postId;
    private Integer tagId;
}