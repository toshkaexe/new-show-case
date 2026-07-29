package com.example.newshowcase.repository;

import com.example.newshowcase.dto.PaginationOutput;
import com.example.newshowcase.dto.PaginationParams;
import com.example.newshowcase.dto.PostOutputModel;
import com.example.newshowcase.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostsQueryRepository {

    private final MongoTemplate mongoTemplate;

    public PostsQueryRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public PaginationOutput<PostOutputModel> getAll(PaginationParams pagination) {
        Query query = new Query();

        if (pagination.getSearchNameTerm() != null) {
            query.addCriteria(Criteria.where("title").regex(pagination.getSearchNameTerm(), "i"));
        }

        long totalCount = mongoTemplate.count(query, Post.class);

        query.with(Sort.by(pagination.getSortDirection(), pagination.getSortBy()));
        query.skip(pagination.getSkipCount());
        query.limit(pagination.getPageSize());

        List<Post> posts = mongoTemplate.find(query, Post.class);
        List<PostOutputModel> mapped = posts.stream().map(PostOutputModel::from).toList();

        return new PaginationOutput<>(mapped, pagination.getPageNumber(), pagination.getPageSize(), totalCount);
    }

    public PaginationOutput<PostOutputModel> getPostsForBlog(String blogId, PaginationParams pagination) {
        Query query = new Query(Criteria.where("blogId").is(blogId));

        long totalCount = mongoTemplate.count(query, Post.class);

        query.with(Sort.by(pagination.getSortDirection(), pagination.getSortBy()));
        query.skip(pagination.getSkipCount());
        query.limit(pagination.getPageSize());

        List<Post> posts = mongoTemplate.find(query, Post.class);
        List<PostOutputModel> mapped = posts.stream().map(PostOutputModel::from).toList();

        return new PaginationOutput<>(mapped, pagination.getPageNumber(), pagination.getPageSize(), totalCount);
    }
}
