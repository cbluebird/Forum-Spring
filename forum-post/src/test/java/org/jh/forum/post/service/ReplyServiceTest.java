package org.jh.forum.post.service;

import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.service.impl.ReplyServiceImpl;
import org.jh.forum.post.vo.ReplyVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyServiceTest {

    @Autowired
    private ReplyServiceImpl replyService;

    @Test
    public void testAddReply() {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setPostId(1);
        replyDTO.setContent("This is a test reply.");
        replyDTO.setRoot(0);
        replyDTO.setParent(0);
        replyService.addReply("1", replyDTO);
        Reply reply = replyService.ensureReplyExist(1);
        assertNotNull(reply);
        assertEquals("This is a test reply.", reply.getContent());
    }

    @Test
    public void testDeleteReply() {
        replyService.deleteReply("1", 1);
        assertThrows(BizException.class, () -> replyService.ensureReplyExist(1));
    }

    @Test
    public void testGetReplyList() {
        Pagination<ReplyVO> replyList = replyService.getReplyList("1", 1);
        assertNotNull(replyList);
        assertFalse(replyList.getList().isEmpty());
    }
}