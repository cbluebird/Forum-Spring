package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jh.forum.post.constant.PostContentType;

@Data
@TableName("post_content")
public class PostContent {
    @TableId
    private Integer id;
    private Integer postId;
    private String content;
    private PostContentType type;
    private Boolean isDel;
}