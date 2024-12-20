package org.jh.forum.post.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {
    public final String HOUR_KEY = "hot_post_hour_key";
    public final String DAY_KEY = "hot_post_day_key";

    @Autowired
    private Jedis jedis;

    @PostConstruct
    public void init() {
        System.out.println("TaskService init");
        new Thread(this::refreshData).start();
    }

    public void setData(String postId) {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        jedis.zincrby(HOUR_KEY + hour, 1, postId);
    }

    public void refreshDay() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        List<String> otherKeys = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String key = HOUR_KEY + (hour - i);
            otherKeys.add(key);
        }

        if (!otherKeys.isEmpty()) {
            jedis.zunionstore(DAY_KEY, otherKeys.toArray(new String[0]));
        }

        for (String key : otherKeys) {
            jedis.expire(key, TimeUnit.DAYS.toSeconds(3));
        }
    }

    public void refreshData() {
        while (true) {
            this.refreshDay();
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}