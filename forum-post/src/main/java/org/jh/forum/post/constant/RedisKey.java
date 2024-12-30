package org.jh.forum.post.constant;

public interface RedisKey {
    String USER_POST_UPVOTE = "user:post:upvote:";
    String USER_REPLY_UPVOTE = "user:reply:upvote:";
    String USER_COLLECT = "user:collect:";
    String HOT_POST_HOUR = "hot_post_hour:";
    String HOT_POST_DAY = "hot_post_day";
    String HOT_TAG_HOUR = "hot_tag_hour:";
    String HOT_TAG_DAY = "hot_tag_day";
}