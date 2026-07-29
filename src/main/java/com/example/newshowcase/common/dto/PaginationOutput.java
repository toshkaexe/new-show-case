package com.example.newshowcase.common.dto;

import java.util.List;

public class PaginationOutput<T> {

    private int pagesCount;
    private int page;
    private int pageSize;
    private long totalCount;
    private List<T> items;

    public PaginationOutput(List<T> items, int page, int pageSize, long totalCount) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.pagesCount = (int) Math.ceil((double) totalCount / pageSize);
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<T> getItems() {
        return items;
    }
}
