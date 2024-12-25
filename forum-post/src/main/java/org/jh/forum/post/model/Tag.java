package org.jh.forum.post.model;

import lombok.Data;

import java.util.Date;

@Data
public class Tag {
    private Integer id;
    private Integer userId;
    private String tag;
    private Integer quoteNum;
    private Date createdOn;
}