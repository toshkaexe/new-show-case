package com.example.newshowcase.controller;

import com.example.newshowcase.dto.BlogOutputModel;
import com.example.newshowcase.dto.CreateBlogRequest;
import com.example.newshowcase.dto.CreatePostForBlogRequest;
import com.example.newshowcase.dto.PaginationOutput;
import com.example.newshowcase.dto.PaginationParams;
import com.example.newshowcase.dto.PostOutputModel;
import com.example.newshowcase.model.Blog;
import com.example.newshowcase.repository.BlogsQueryRepository;
import com.example.newshowcase.repository.PostsQueryRepository;
import com.example.newshowcase.service.BlogsService;
import com.example.newshowcase.service.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Blogs")
@RestController
@RequestMapping("/blogs")
public class BlogsController {

    private static final List<String> SORTING_PROPERTIES = List.of("name");

    private final BlogsService blogsService;
    private final PostsService postsService;
    private final BlogsQueryRepository blogsQueryRepository;
    private final PostsQueryRepository postsQueryRepository;

    public BlogsController(BlogsService blogsService, PostsService postsService,
                           BlogsQueryRepository blogsQueryRepository, PostsQueryRepository postsQueryRepository) {
        this.blogsService = blogsService;
        this.postsService = postsService;
        this.blogsQueryRepository = blogsQueryRepository;
        this.postsQueryRepository = postsQueryRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogOutputModel create(@Valid @RequestBody CreateBlogRequest createBlogDto) {
        return blogsService.create(createBlogDto);
    }

    @GetMapping
    public PaginationOutput<BlogOutputModel> findAll(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String searchNameTerm
    ) {
        PaginationParams pagination = new PaginationParams(
                pageNumber, pageSize, sortBy, sortDirection,
                null, null, searchNameTerm, SORTING_PROPERTIES
        );
        return blogsQueryRepository.getAll(pagination);
    }

    @GetMapping("/{id}")
    public BlogOutputModel getBlogById(@PathVariable String id) {
        Blog blog = blogsService.findBlogById(id);
        if (blog == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog with ID " + id + " not found");
        }
        return BlogOutputModel.from(blog);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Valid @RequestBody CreateBlogRequest updateBlogDto) {
        boolean result = blogsService.update(id, updateBlogDto);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't update Blog with ID " + id);
        }
    }

    @DeleteMapping("/{blogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String blogId) {
        boolean result = blogsService.remove(blogId);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't delete Blog with ID " + blogId);
        }
    }

    @PostMapping("/{blogId}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutputModel createPostForBlog(@PathVariable String blogId,
                                             @Valid @RequestBody CreatePostForBlogRequest newPost) {
        return postsService.createPostForBlog(blogId, newPost);
    }

    @GetMapping("/{blogId}/posts")
    public PaginationOutput<PostOutputModel> getPostsForBlog(
            @PathVariable String blogId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        PaginationParams pagination = new PaginationParams(
                pageNumber, pageSize, sortBy, sortDirection,
                null, null, null, SORTING_PROPERTIES
        );
        PaginationOutput<PostOutputModel> posts = postsQueryRepository.getPostsForBlog(blogId, pagination);
        if (posts.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No posts found for Blog with ID " + blogId);
        }
        return posts;
    }
}
