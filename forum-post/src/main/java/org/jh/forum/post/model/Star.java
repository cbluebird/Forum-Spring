package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("post_star")
public class Star {
    @TableId
    private Integer id;
    private Integer postId;
    private Integer userId;
    private Date createdOn;
    private Date deletedOn;
}