package com.example.newshowcase.service;

import com.example.newshowcase.dto.BlogOutputModel;
import com.example.newshowcase.dto.CreateBlogRequest;
import com.example.newshowcase.model.Blog;
import com.example.newshowcase.repository.BlogsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BlogsService {

    private static final Logger log = LoggerFactory.getLogger(BlogsService.class);

    private final BlogsRepository blogsRepository;

    public BlogsService(BlogsRepository blogsRepository) {
        this.blogsRepository = blogsRepository;
    }

    public BlogOutputModel create(CreateBlogRequest createModel) {
        Blog blog = new Blog(createModel.getName(), createModel.getDescription(), createModel.getWebsiteUrl());
        Blog saved = blogsRepository.save(blog);
        log.info("Blog created: id={}, name='{}'", saved.getId(), saved.getName());
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
        log.info("Blog updated: id={}", id);
        return true;
    }

    public boolean remove(String id) {
        if (!blogsRepository.existsById(id)) {
            return false;
        }
        blogsRepository.deleteById(id);
        log.info("Blog deleted: id={}", id);
        return true;
    }

    public void deleteAll() {
        blogsRepository.deleteAll();
        log.warn("All blogs deleted");
    }
}
