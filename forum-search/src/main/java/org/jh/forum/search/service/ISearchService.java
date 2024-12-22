package org.jh.forum.search.service;

import com.meilisearch.sdk.model.SearchResultPaginated;
import org.jh.forum.search.vo.SearchTagVO;

import java.util.List;

public interface ISearchService {
    List<SearchTagVO> searchTag(String q);

    SearchResultPaginated searchPost(String q, Long pageNum, Long pageSize);
}