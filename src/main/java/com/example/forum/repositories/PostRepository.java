package com.example.forum.repositories;

import com.example.forum.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getAllPosts();

    Post getPostById(int id);

    Post getPostByIdWithComments(int id);

    void createPost(Post post);

    void updatePost(Post post);

    void deletePostById(int id);
}