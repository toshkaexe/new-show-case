package com.example.newshowcase.controller;

import com.example.newshowcase.dto.CreateUserRequest;
import com.example.newshowcase.dto.PaginationOutput;
import com.example.newshowcase.dto.PaginationParams;
import com.example.newshowcase.dto.UserOutputModel;
import com.example.newshowcase.repository.UsersQueryRepository;
import com.example.newshowcase.service.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final List<String> SORTING_PROPERTIES = List.of("login", "email");

    private final UsersService usersService;
    private final UsersQueryRepository usersQueryRepository;

    public UsersController(UsersService usersService, UsersQueryRepository usersQueryRepository) {
        this.usersService = usersService;
        this.usersQueryRepository = usersQueryRepository;
    }

    @GetMapping
    public PaginationOutput<UserOutputModel> getAll(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String searchLoginTerm,
            @RequestParam(required = false) String searchEmailTerm,
            @RequestParam(required = false) String searchNameTerm
    ) {
        PaginationParams pagination = new PaginationParams(
                pageNumber, pageSize, sortBy, sortDirection,
                searchLoginTerm, searchEmailTerm, searchNameTerm, SORTING_PROPERTIES
        );
        return usersQueryRepository.getAll(pagination);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputModel create(@Valid @RequestBody CreateUserRequest createModel) {
        String createdUserId = usersService.create(
                createModel.getLogin(),
                createModel.getPassword(),
                createModel.getEmail()
        );
        return usersQueryRepository.getById(createdUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/{id}")
    public UserOutputModel getUserById(@PathVariable String id) {
        return usersQueryRepository.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with id " + id + " not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        boolean result = usersService.removeById(id);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        }
    }
}
