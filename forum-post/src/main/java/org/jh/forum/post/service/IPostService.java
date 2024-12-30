package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.vo.PostVO;

public interface IPostService extends IService<Post> {
    void addPost(String userId, PostDTO postDTO);

    void deletePost(String userId, Integer postId);

    PostVO getPost(String userId, Integer id);

    Pagination<PostVO> getPostListByCategory(String userId, Integer categoryId, Long pageNum, Long pageSize);

    Pagination<PostVO> getPostListByTag(String userId, Integer tagId, Long pageNum, Long pageSize);

    Pagination<PostVO> getPostListByUser(String userId, Integer authorId, Long pageNum, Long pageSize);

    Pagination<PostVO> getUpvotePostList(String userId, Long pageNum, Long pageSize);

    Pagination<PostVO> getCollectPostList(String userId, Long pageNum, Long pageSize);

    Pagination<PostVO> getHotPostOfDay(String userId, Long pageNum, Long pageSize);

    Post ensurePostExist(Integer id);
}