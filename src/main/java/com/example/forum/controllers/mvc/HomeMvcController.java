package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.services.PostService;
import com.example.forum.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeMvcController {

    private final PostService postService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(PostService postService,
                             UserService userService,
                             AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/")
    public String showHome(Model model, HttpSession session) {

        // Recent posts: sort by createdAt desc and take first 10
        List<Post> allPostsSorted = postService.getAllPostsSorted("createdAt", "desc");
        List<Post> recentPosts = allPostsSorted.stream()
                .limit(10)
                .toList();

        // Top commented posts using new service method
        List<Post> topCommentedPosts = postService.getTopCommentedPosts(10);

        // Total posts
        int totalPosts = allPostsSorted.size();

        // Total users
        int totalUsers = userService.getUsers().size();

        // Current user from session
        User currentUser = null;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException ignored) {
            // not logged in → keep currentUser = null
        }

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("topCommentedPosts", topCommentedPosts);
        model.addAttribute("currentUser", currentUser);

        return "HomeView";
    }
}
