package org.jh.forum.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class Pagination<T> {
    private Long pageNum;
    private Long pageSize;
    private Long total;
    private List<T> list;

    public static <T> Pagination<T> of(IPage<T> page) {
        Pagination<T> pagination = new Pagination<>();
        pagination.setPageNum(page.getCurrent());
        pagination.setPageSize(page.getSize());
        pagination.setTotal(page.getTotal());
        pagination.setList(page.getRecords());
        return pagination;
    }
}