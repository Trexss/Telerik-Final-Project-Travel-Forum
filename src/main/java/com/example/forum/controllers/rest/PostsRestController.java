package com.example.forum.controllers.rest;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.CommentMapper;
import com.example.forum.helpers.PostMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/posts")
public class PostsRestController {
    private final PostsService postsService;
    private final AuthenticationHelper authenticationHelper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public PostsRestController(PostsService postsService, AuthenticationHelper authenticationHelper, PostMapper postMapper, CommentMapper commentMapper) {
        this.postsService = postsService;
        this.authenticationHelper = authenticationHelper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postsService.getAllPosts().stream()
                .map(postMapper::toDto)
                .toList();
    }
    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable int id) {
        try {
            return postMapper.toDto(postsService.getPostById(id));
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public PostDto createPost(@Valid @RequestBody PostDto postDto) {
        //toDo authentication
        Post post = postMapper.fromDto(postDto);
        return postMapper.toDto(postsService.createPost(post));

    }
    @PutMapping("/{id}")
    public PostDto updatePost(@PathVariable int id, @Valid @RequestBody PostDto postDto) {
        //toDo authentication
        //toDo authorization only own posts
        Post post = postMapper.fromDto(postDto);
        try {
            return postMapper.toDto(postsService.updatePost(id, post));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        //toDo authentication
        //toDo authorization only own posts
        try {
            postsService.deletePost(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/{id}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable int id) {
        //toDo authentication
        try {
            return postsService.getCommentsByPostId(id).stream()
                    .map(commentMapper::toDto)
                    .toList();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping("/{id}/comments")
    public CommentDto addCommentToPost(@PathVariable int id, @Valid @RequestBody CommentDto commentDto) {
        //toDo authentication
        Comment comment = commentMapper.fromDto(commentDto);
        try {
            return commentMapper.toDto(postsService.addCommentToPost(id, comment));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    //toDo like other peoples Posts
}
