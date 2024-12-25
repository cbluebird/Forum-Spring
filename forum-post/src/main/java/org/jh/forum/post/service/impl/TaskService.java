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
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        System.out.println("TaskService init");
        new Thread(this::refreshData).start();
    }

    public void setData(String postId) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        redisTemplate.opsForZSet().incrementScore(RedisKey.HOT_POST_HOUR + hour, postId, 1);
    }

    public void refreshDay() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        List<String> otherKeys = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String key = RedisKey.HOT_POST_HOUR + (hour - i);
            otherKeys.add(key);
        }

        if (!otherKeys.isEmpty()) {
            redisTemplate.opsForZSet().unionAndStore(otherKeys.get(0), otherKeys.subList(1, otherKeys.size()), RedisKey.HOT_POST_DAY);
        }

        for (String key : otherKeys) {
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