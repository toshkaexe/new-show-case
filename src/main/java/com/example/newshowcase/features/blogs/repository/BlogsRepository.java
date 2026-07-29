package com.example.newshowcase.features.blogs.repository;

import com.example.newshowcase.features.blogs.domain.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogsRepository extends MongoRepository<Blog, String> {
}
