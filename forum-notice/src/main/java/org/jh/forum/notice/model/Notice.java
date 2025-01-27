package org.jh.forum.notice.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notice")
public class Notice {
    @TableId
    private Integer id;
    private Integer userId;
    private String content;
    private LocalDateTime createdOn;
}