package org.jh.forum.notice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.notice.model.Notice;
import org.jh.forum.notice.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @PostMapping("/add")
    public void addNotice(@RequestBody Notice notice, @RequestHeader("X-User-ID") String userId) {
        notice.setUserId(Long.valueOf(userId));
        notice.setCreatedOn(new Date());
        noticeService.save(notice);
    }

    @DeleteMapping("/del")
    public void deleteNotice(@RequestParam Long noticeId) {
        noticeService.removeById(noticeId);
    }

    @PutMapping("/update")
    public void updateNotice(@RequestBody Notice notice) {
        noticeService.updateById(notice);
    }

    @GetMapping("/get")
    public List<Notice> getNotices(@RequestParam int page_num, @RequestParam int page_size) {
        Page<Notice> noticePage = noticeService.page(
                new Page<>(page_num, page_size),
                new QueryWrapper<Notice>().orderByDesc("created_on")
        );
        return noticePage.getRecords();
    }
}