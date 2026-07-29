package com.example.newshowcase.repository;

import com.example.newshowcase.model.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogsRepository extends MongoRepository<Blog, String> {
}
