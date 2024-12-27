package org.jh.forum.post.service.impl;

import cn.hutool.cron.CronException;
import jakarta.annotation.PostConstruct;
import org.jh.forum.post.constant.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @PostConstruct
    public void init() {
        System.out.println("TaskService init");
        new Thread(this::refreshData).start();
    }

    public void setPostData(Integer postId) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        redisTemplate.opsForZSet().incrementScore(RedisKey.HOT_POST_HOUR + hour, postId, 1);
    }

    public void delPostData(Integer postId) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        redisTemplate.opsForZSet().incrementScore(RedisKey.HOT_POST_HOUR + hour, postId, -1);
    }

    public void delPostKey(Integer postId) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        List<String> hourKeys = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String key = RedisKey.HOT_POST_HOUR + (hour - i);
            hourKeys.add(key);
        }
        if (!hourKeys.isEmpty()) {
            for (String key : hourKeys) {
                redisTemplate.opsForZSet().remove(key, postId);
            }
        }
        redisTemplate.opsForZSet().remove(RedisKey.HOT_POST_DAY, postId);
    }

    public void setTagData(Integer[] tagIds) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        for (Integer tagId : tagIds) {
            redisTemplate.opsForZSet().incrementScore(RedisKey.HOT_TAG_HOUR + hour, tagId, 1);
        }
    }

    public void delTagData(Integer[] tagIds) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        for (Integer tagId : tagIds) {
            redisTemplate.opsForZSet().incrementScore(RedisKey.HOT_TAG_HOUR + hour, tagId, -1);
        }
    }

    public void refreshDay() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        List<String> postKeys = new ArrayList<>();
        List<String> tagKeys = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String postKey = RedisKey.HOT_POST_HOUR + (hour - i);
            String tagKey = RedisKey.HOT_TAG_HOUR + (hour - i);
            postKeys.add(postKey);
            tagKeys.add(tagKey);
        }

        if (!postKeys.isEmpty()) {
            redisTemplate.opsForZSet().unionAndStore(postKeys.get(0), postKeys.subList(1, postKeys.size()), RedisKey.HOT_POST_DAY);
        }
        if (!tagKeys.isEmpty()) {
            redisTemplate.opsForZSet().unionAndStore(tagKeys.get(0), tagKeys.subList(1, tagKeys.size()), RedisKey.HOT_TAG_DAY);
        }

        for (String key : postKeys) {
            redisTemplate.expire(key, 3, TimeUnit.DAYS);
        }
        for (String key : tagKeys) {
            redisTemplate.expire(key, 3, TimeUnit.DAYS);
        }
    }

    public void refreshData() {
        while (true) {
            this.refreshDay();
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                throw new CronException(e);
            }
        }
    }
}