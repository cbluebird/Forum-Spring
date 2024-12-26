package org.jh.forum.search.service.impl;

import cn.hutool.json.JSONUtil;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.MatchingStrategy;
import com.meilisearch.sdk.model.SearchResultPaginated;
import org.jh.forum.search.constant.IndexConstant;
import org.jh.forum.search.service.ISearchService;
import org.jh.forum.search.vo.SearchTagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private Client client;

    @Override
    public List<SearchTagVO> searchTag(String q) {
        Index index = client.index(IndexConstant.TAG_INDEX);
        SearchRequest searchRequest = SearchRequest.builder()
                .q(q)
                .page(1)
                .hitsPerPage(10)
                .matchingStrategy(MatchingStrategy.ALL)
                .attributesToSearchOn(new String[]{"tag"})
                .attributesToRetrieve(new String[]{"id", "tag"})
                .build();
        SearchResultPaginated srp = (SearchResultPaginated) index.search(searchRequest);
        return JSONUtil.toList(JSONUtil.toJsonStr(srp.getHits()), SearchTagVO.class);
    }

    @Override
    public SearchResultPaginated searchPost(String q, Long pageNum, Long pageSize) {
        Index index = client.index(IndexConstant.POST_INDEX);
        SearchRequest searchRequest = SearchRequest.builder()
                .q(q)
                .page(Math.toIntExact(pageNum))
                .hitsPerPage(Math.toIntExact(pageSize))
                .matchingStrategy(MatchingStrategy.LAST)
                .attributesToSearchOn(new String[]{"title"})
                .attributesToCrop(new String[]{"content"})
                .cropLength(100)
                .attributesToHighlight(new String[]{"content"})
                .attributesToRetrieve(new String[]{"id", "user_id", "title", "content", "view_count", "upvote_count", "reply_count", "collection_count", "share_count", "ip", "ip_loc", "modified_on"})
                .build();
        return (SearchResultPaginated) index.search(searchRequest);
    }
}