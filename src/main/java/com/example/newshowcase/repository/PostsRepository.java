package com.example.newshowcase.repository;

import com.example.newshowcase.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends MongoRepository<Post, String> {

    List<Post> findByBlogId(String blogId);
}
