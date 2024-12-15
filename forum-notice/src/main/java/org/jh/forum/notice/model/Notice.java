package org.jh.forum.notice.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notice")
public class Notice {
    @TableId
    private Long id;
    private Long userId;
    private String content;
    private Date createdOn;
}