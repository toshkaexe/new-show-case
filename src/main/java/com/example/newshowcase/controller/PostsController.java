package com.example.newshowcase.controller;

import com.example.newshowcase.dto.CreatePostRequest;
import com.example.newshowcase.dto.PaginationOutput;
import com.example.newshowcase.dto.PaginationParams;
import com.example.newshowcase.dto.PostOutputModel;
import com.example.newshowcase.dto.UpdatePostRequest;
import com.example.newshowcase.repository.PostsQueryRepository;
import com.example.newshowcase.service.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Posts")
@RestController
@RequestMapping("/posts")
public class PostsController {

    private static final List<String> SORTING_PROPERTIES = List.of("title");

    private final PostsService postsService;
    private final PostsQueryRepository postsQueryRepository;

    public PostsController(PostsService postsService, PostsQueryRepository postsQueryRepository) {
        this.postsService = postsService;
        this.postsQueryRepository = postsQueryRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutputModel create(@Valid @RequestBody CreatePostRequest newPost) {
        return postsService.create(newPost);
    }

    @GetMapping
    public PaginationOutput<PostOutputModel> findAll(
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
        return postsQueryRepository.getAll(pagination);
    }

    @GetMapping("/{id}")
    public PostOutputModel findOne(@PathVariable String id) {
        PostOutputModel post = postsService.findOne(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with ID " + id + " not found");
        }
        return post;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Valid @RequestBody UpdatePostRequest updatePostDto) {
        boolean result = postsService.update(id, updatePostDto);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't update Post with ID " + id);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable String id) {
        boolean result = postsService.remove(id);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't delete Post with ID " + id);
        }
    }
}
