package org.jh.forum.post.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Tag {
    private Integer id;
    private Integer userId;
    private String name;
    private LocalDateTime createdOn;
}