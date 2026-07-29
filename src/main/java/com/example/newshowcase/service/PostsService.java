package com.example.newshowcase.service;

import com.example.newshowcase.dto.CreatePostForBlogRequest;
import com.example.newshowcase.dto.CreatePostRequest;
import com.example.newshowcase.dto.PostOutputModel;
import com.example.newshowcase.dto.UpdatePostRequest;
import com.example.newshowcase.model.Blog;
import com.example.newshowcase.model.ExtendedLikesInfo;
import com.example.newshowcase.model.NewestLike;
import com.example.newshowcase.model.Post;
import com.example.newshowcase.repository.BlogsRepository;
import com.example.newshowcase.repository.PostsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private static final Logger log = LoggerFactory.getLogger(PostsService.class);

    private final PostsRepository postsRepository;
    private final BlogsRepository blogsRepository;

    public PostsService(PostsRepository postsRepository, BlogsRepository blogsRepository) {
        this.postsRepository = postsRepository;
        this.blogsRepository = blogsRepository;
    }

    public PostOutputModel create(CreatePostRequest data) {
        Blog blog = blogsRepository.findById(data.getBlogId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Blog with ID " + data.getBlogId() + " not found"));

        String publishedAt = Instant.now().toString();

        Post post = new Post();
        post.setTitle(data.getTitle());
        post.setShortDescription(data.getShortDescription());
        post.setContent(data.getContent());
        post.setBlogId(data.getBlogId());
        post.setBlogName(blog.getName());
        post.setCreatedAt(publishedAt);
        post.setExtendedLikesInfo(new ExtendedLikesInfo(0, 0, "None",
                List.of(new NewestLike(publishedAt, "string", "string"))));

        Post saved = postsRepository.save(post);
        log.info("Post created: id={}, title='{}', blogId={}", saved.getId(), saved.getTitle(), saved.getBlogId());
        return PostOutputModel.from(saved);
    }

    public PostOutputModel createPostForBlog(String blogId, CreatePostForBlogRequest newPost) {
        Blog blog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Blog with ID " + blogId + " not found"));

        String publishedAt = Instant.now().toString();

        Post post = new Post();
        post.setTitle(newPost.getTitle());
        post.setShortDescription(newPost.getShortDescription());
        post.setContent(newPost.getContent());
        post.setBlogId(blogId);
        post.setBlogName(blog.getName());
        post.setCreatedAt(publishedAt);
        post.setExtendedLikesInfo(new ExtendedLikesInfo(0, 0, "None",
                List.of(new NewestLike(publishedAt, "string", "string"))));

        Post saved = postsRepository.save(post);
        log.info("Post created for blog: id={}, title='{}', blogId={}", saved.getId(), saved.getTitle(), blogId);
        return PostOutputModel.from(saved);
    }

    public PostOutputModel findOne(String id) {
        Optional<Post> post = postsRepository.findById(id);
        return post.map(PostOutputModel::from).orElse(null);
    }

    public boolean remove(String id) {
        if (!postsRepository.existsById(id)) {
            return false;
        }
        postsRepository.deleteById(id);
        log.info("Post deleted: id={}", id);
        return true;
    }

    public boolean update(String id, UpdatePostRequest updateModel) {
        Optional<Post> optPost = postsRepository.findById(id);
        if (optPost.isEmpty()) {
            return false;
        }

        blogsRepository.findById(updateModel.getBlogId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Blog with ID " + updateModel.getBlogId() + " not found"));

        Post post = optPost.get();
        post.setTitle(updateModel.getTitle());
        post.setShortDescription(updateModel.getShortDescription());
        post.setContent(updateModel.getContent());
        post.setBlogId(updateModel.getBlogId());
        postsRepository.save(post);
        log.info("Post updated: id={}", id);
        return true;
    }

    public void deleteAll() {
        postsRepository.deleteAll();
        log.warn("All posts deleted");
    }
}
