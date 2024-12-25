package org.jh.forum.post.constant;

public interface RedisKey {
    String USER_POST_UPVOTE = "user:post:upvote:";
    String USER_REPLY_UPVOTE = "user:reply:upvote:";
    String USER_COLLECTION = "user:collection:";
    String HOT_POST_HOUR = "hot_post_hour:";
    String HOT_POST_DAY = "hot_post_day";
}