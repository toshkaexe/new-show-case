package com.example.newshowcase.dto;

import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationParams {

    private final int pageNumber;
    private final int pageSize;
    private final String sortBy;
    private final Sort.Direction sortDirection;
    private final String searchLoginTerm;
    private final String searchEmailTerm;
    private final String searchNameTerm;

    public PaginationParams(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection,
            String searchLoginTerm,
            String searchEmailTerm,
            String searchNameTerm,
            List<String> allowedSortProperties
    ) {
        this.pageNumber = pageNumber != null ? pageNumber : 1;
        this.pageSize = pageSize != null ? pageSize : 10;
        this.sortDirection = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        this.searchLoginTerm = searchLoginTerm;
        this.searchEmailTerm = searchEmailTerm;
        this.searchNameTerm = searchNameTerm;

        if (sortBy != null && allowedSortProperties.contains(sortBy)) {
            this.sortBy = sortBy;
        } else {
            this.sortBy = "createdAt";
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public int getSkipCount() {
        return (pageNumber - 1) * pageSize;
    }

    public String getSearchLoginTerm() {
        return searchLoginTerm;
    }

    public String getSearchEmailTerm() {
        return searchEmailTerm;
    }

    public String getSearchNameTerm() {
        return searchNameTerm;
    }
}
