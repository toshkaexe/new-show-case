package com.example.newshowcase.controller;

import com.example.newshowcase.service.BlogsService;
import com.example.newshowcase.service.PostsService;
import com.example.newshowcase.service.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Removing")
@RestController
@RequestMapping("/all-data")
public class DeletingController {

    private final UsersService usersService;
    private final BlogsService blogsService;
    private final PostsService postsService;

    public DeletingController(UsersService usersService, BlogsService blogsService, PostsService postsService) {
        this.usersService = usersService;
        this.blogsService = blogsService;
        this.postsService = postsService;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        usersService.deleteAll();
        blogsService.deleteAll();
        postsService.deleteAll();
    }
}
