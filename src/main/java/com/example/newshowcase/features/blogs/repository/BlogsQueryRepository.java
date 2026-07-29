package com.example.newshowcase.features.blogs.repository;

import com.example.newshowcase.common.dto.PaginationOutput;
import com.example.newshowcase.common.dto.PaginationParams;
import com.example.newshowcase.features.blogs.domain.Blog;
import com.example.newshowcase.features.blogs.dto.BlogOutputModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogsQueryRepository {

    private final MongoTemplate mongoTemplate;

    public BlogsQueryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public PaginationOutput<BlogOutputModel> getAll(PaginationParams pagination) {
        Query query = new Query();

        if (pagination.getSearchNameTerm() != null) {
            query.addCriteria(Criteria.where("name").regex(pagination.getSearchNameTerm(), "i"));
        }

        long totalCount = mongoTemplate.count(query, Blog.class);

        query.with(Sort.by(pagination.getSortDirection(), pagination.getSortBy()));
        query.skip(pagination.getSkipCount());
        query.limit(pagination.getPageSize());

        List<Blog> blogs = mongoTemplate.find(query, Blog.class);
        List<BlogOutputModel> mapped = blogs.stream().map(BlogOutputModel::from).toList();

        return new PaginationOutput<>(mapped, pagination.getPageNumber(), pagination.getPageSize(), totalCount);
    }
}
