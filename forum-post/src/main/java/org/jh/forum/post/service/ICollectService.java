package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.model.Collect;

public interface ICollectService extends IService<Collect> {
    void collectPost(String userId, CollectDTO collectDTO);

    void uncollectPost(String userId, CollectDTO collectDTO);
}