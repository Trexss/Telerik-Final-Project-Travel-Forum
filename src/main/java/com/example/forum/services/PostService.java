package com.example.forum.services;

import com.example.forum.models.Post;
import com.example.forum.models.User;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts();

    Post getPostById(int id);

    Post getPostByIdWithComments(int id);

    void createPost(Post post, User user);

    void updatePost(Post post, User user);

    void deletePostById(int id, User user);

    void incrementPostLikes(int id);

    List<Post> getAllPostsSorted(String sortBy, String order);

    List<Post> getTopCommentedPosts(int limit);
}
