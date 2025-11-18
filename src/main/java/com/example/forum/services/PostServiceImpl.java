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
    public List<Post> get() {
        return postRepository.get();
    }

    @Override
    public Post get(int id) {
        return postRepository.get(id);
    }

    @Override
    public void create(Post post, User user) {
        post.setUser(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        Post existingPost = postRepository.get(post.getId());

        // Check if user is the owner or admin
        if (existingPost.getUser().getId() != user.getId() && !user.isAdmin()) {
            throw new AuthorizationException("Only the creator or admin can update this post.");
        }

        postRepository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        Post existingPost = postRepository.get(id);

        // Check if user is the owner or admin
        if (existingPost.getUser().getId() != user.getId() && !user.isAdmin()) {
            throw new AuthorizationException("Only the creator or admin can delete this post.");
        }

        postRepository.delete(id);
    }

    @Override
    public void addLike(int id) {
        Post post = postRepository.get(id);
        post.setLikes(post.getLikes() + 1);
        postRepository.update(post);
    }
}
