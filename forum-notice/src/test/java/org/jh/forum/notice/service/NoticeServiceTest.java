package org.jh.forum.notice.service;

import org.jh.forum.notice.model.Notice;
import org.jh.forum.notice.service.impl.NoticeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NoticeServiceTest {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Test
    public void testCreateNotice() {
        Notice notice = new Notice();
        notice.setUserId(1);
        notice.setContent("This is a test notice.");
        notice.setCreatedOn(LocalDateTime.now());

        boolean isSaved = noticeService.save(notice);
        assertTrue(isSaved);
    }

    @Test
    public void testFindNoticeById() {
        Notice notice = new Notice();
        notice.setUserId(1);
        notice.setContent("This is a test notice.");
        notice.setCreatedOn(LocalDateTime.now());

        noticeService.save(notice);
        Notice foundNotice = noticeService.getById(notice.getId());
        assertNotNull(foundNotice);
        assertEquals(notice.getId(), foundNotice.getId());
    }

    @Test
    public void testFindAllNotices() {
        List<Notice> notices = noticeService.list();
        assertNotNull(notices);
        assertFalse(notices.isEmpty());
    }

    @Test
    public void testDeleteNoticeById() {
        Notice notice = new Notice();
        notice.setUserId(1);
        notice.setContent("This is a test notice.");
        notice.setCreatedOn(LocalDateTime.now());

        noticeService.save(notice);
        boolean isDeleted = noticeService.removeById(notice.getId());
        assertTrue(isDeleted);

        Notice foundNotice = noticeService.getById(notice.getId());
        assertNull(foundNotice);
    }
}