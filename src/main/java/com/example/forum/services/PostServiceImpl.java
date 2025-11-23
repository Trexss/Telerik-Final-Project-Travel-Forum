package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

    @Override
    public void createPost(Post post, User user) {
        post.setUser(user);
        postRepository.createPost(post);
    }

    @Override
    public void updatePost(Post post, User user) {
        Post existingPost = postRepository.getPostById(post.getId());

        // Check if user is the owner or admin
        if (existingPost.getUser().getId() != user.getId() && !user.isAdmin()) {
            throw new AuthorizationException("Only the creator or admin can update this post.");
        }

        postRepository.updatePost(post);
    }

    @Override
    public void deletePostById(int id, User user) {
        Post existingPost = postRepository.getPostById(id);

        // Check if user is the owner or admin
        if (existingPost.getUser().getId() != user.getId() && !user.isAdmin()) {
            throw new AuthorizationException("Only the creator or admin can delete this post.");
        }

        postRepository.deletePostById(id);
    }

    @Override
    public void incrementPostLikes(int id) {
        Post post = postRepository.getPostById(id);
        post.setLikes(post.getLikes() + 1);
        postRepository.updatePost(post);
    }
}