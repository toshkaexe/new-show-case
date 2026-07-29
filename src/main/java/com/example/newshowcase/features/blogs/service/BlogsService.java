package com.example.newshowcase.features.blogs.service;

import com.example.newshowcase.features.blogs.domain.Blog;
import com.example.newshowcase.features.blogs.dto.BlogOutputModel;
import com.example.newshowcase.features.blogs.dto.CreateBlogRequest;
import com.example.newshowcase.features.blogs.repository.BlogsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BlogsService {

    private final BlogsRepository blogsRepository;

    public BlogsService(BlogsRepository blogsRepository) {
        this.blogsRepository = blogsRepository;
    }

    public BlogOutputModel create(CreateBlogRequest createModel) {
        Blog blog = new Blog(createModel.getName(), createModel.getDescription(), createModel.getWebsiteUrl());
        Blog saved = blogsRepository.save(blog);
        return BlogOutputModel.from(saved);
    }

    public Blog findBlogById(String id) {
        return blogsRepository.findById(id).orElse(null);
    }

    public boolean update(String id, CreateBlogRequest updateModel) {
        Optional<Blog> optBlog = blogsRepository.findById(id);
        if (optBlog.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog with ID " + id + " not found");
        }
        Blog blog = optBlog.get();
        blog.setName(updateModel.getName());
        blog.setDescription(updateModel.getDescription());
        blog.setWebsiteUrl(updateModel.getWebsiteUrl());
        blogsRepository.save(blog);
        return true;
    }

    public boolean remove(String id) {
        if (!blogsRepository.existsById(id)) {
            return false;
        }
        blogsRepository.deleteById(id);
        return true;
    }

    public void deleteAll() {
        blogsRepository.deleteAll();
    }
}
