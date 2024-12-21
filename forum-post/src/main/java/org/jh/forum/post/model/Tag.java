package org.jh.forum.post.model;

import lombok.Data;

import java.util.Date;

@Data
public class Tag {
    private Long id;
    private Long userId;
    private String tag;
    private Long quoteNum;
    private Date createdOn;
}