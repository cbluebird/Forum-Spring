package org.jh.forum.post.vo;

import lombok.Data;
import lombok.Setter;
import org.jh.forum.post.constant.Visibility;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostContent;
import org.jh.forum.post.model.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PostVO {
    private Integer id;
    private UserVO user;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer upvoteCount;
    private Integer replyCount;
    private Integer collectionCount;
    private Integer shareCount;
    private Boolean isCollect;
    private Boolean isUpvote;
    private Visibility visibility;
    private String ip;
    private String ipLoc;
    private Date createdOn;
    private Date modifiedOn;
    private List<PostContent> postLink;
    @Setter
    private List<TagVO> tags;

    public void setPostVO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.replyCount = post.getReplyCount();
        this.collectionCount = post.getCollectionCount();
        this.upvoteCount = post.getUpvoteCount();
        this.viewCount = post.getViewCount();
        this.shareCount = post.getShareCount();
        this.visibility = post.getVisibility();
        this.ip = post.getIp();
        this.ipLoc = post.getIpLoc();
        this.createdOn = post.getCreatedOn();
        this.modifiedOn = post.getModifiedOn();
        this.isCollect = false;
        this.isUpvote = false;
    }

    public void setUserVO(Map<String, Object> userMap) {
        UserVO userVO = new UserVO();
        userVO.setId((Integer) userMap.get("id"));
        userVO.setUsername((String) userMap.get("username"));
        userVO.setNickname((String) userMap.get("nickname"));
        userVO.setAvatar((String) userMap.get("avatar"));
        this.user = userVO;
    }

    public void setPostContentVO(List<PostContent> postLink) {
        this.postLink = postLink;
    }

    public void setTags(List<Tag> tags) {
        this.tags = new ArrayList<>();
        for (Tag tag : tags) {
            TagVO tagVO = new TagVO();
            tagVO.setId(tag.getId());
            tagVO.setTag(tag.getTag());
            this.tags.add(tagVO);
        }
    }
}