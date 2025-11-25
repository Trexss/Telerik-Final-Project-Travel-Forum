package com.example.forum.controllers.mvc;

import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.services.PostService;
import com.example.forum.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostMvcController {
    private final PostService postService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public PostMvcController(PostService postService, UserService userService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping
    public String showAllPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "PostsView";
    }


}
