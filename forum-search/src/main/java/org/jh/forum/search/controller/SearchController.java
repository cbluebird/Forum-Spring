package org.jh.forum.search.controller;

import cn.hutool.json.JSONUtil;
import com.meilisearch.sdk.model.SearchResultPaginated;
import jakarta.validation.constraints.NotBlank;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.search.feign.UserFeign;
import org.jh.forum.search.service.ISearchService;
import org.jh.forum.search.vo.SearchPostVO;
import org.jh.forum.search.vo.SearchTagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private ISearchService searchService;
    @Autowired
    private UserFeign userFeign;

    @GetMapping("/tag")
    public List<SearchTagVO> searchTag(@RequestParam("q") @NotBlank(message = "内容不能为空") String q) {
        return searchService.searchTag(q);
    }

    @GetMapping("/post")
    public Pagination<SearchPostVO> searchPost(@RequestParam("q") @NotBlank(message = "内容不能为空") String q,
                                               @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        SearchResultPaginated srp = searchService.searchPost(q, pageNum, pageSize);
        List<HashMap<String, Object>> hits = srp.getHits();
        for (HashMap<String, Object> hit : hits) {
            Map<String, Object> formatted = (Map<String, Object>) hit.get("_formatted");
            hit.put("title", formatted.get("title"));
            hit.put("content", formatted.get("content"));
            long timestamp = Long.parseLong((String) formatted.get("modified_on"));
            Instant instant = Instant.ofEpochSecond(timestamp);
            hit.put("modified_on", LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
            String userId = (String) formatted.get("user_id");
            hit.put("user", userFeign.getUserById(Long.parseLong(userId)));
        }
        List<SearchPostVO> searchPostVOList = JSONUtil.toList(JSONUtil.toJsonStr(hits), SearchPostVO.class);
        return Pagination.of(searchPostVOList, pageNum, pageSize, (long) srp.getTotalHits());
    }
}