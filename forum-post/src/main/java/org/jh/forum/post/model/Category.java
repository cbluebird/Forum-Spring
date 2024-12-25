package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("categories")
public class Category {
    @TableId
    private Integer id;
    private String name;
    private String description;
    private Integer quoteNum;
}
