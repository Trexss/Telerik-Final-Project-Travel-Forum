package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTests {

    private PostRepository postRepository;
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        postService = new PostServiceImpl(postRepository);
    }

    @Test
    void getAllPosts_Should_ReturnPostsFromRepository() {
        // Arrange
        List<Post> expected = List.of(new Post(), new Post());
        when(postRepository.getAllPosts()).thenReturn(expected);

        // Act
        List<Post> result = postService.getAllPosts();

        // Assert
        assertEquals(expected, result);
        verify(postRepository, times(1)).getAllPosts();
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getPostById_Should_CallRepositoryAndReturnPost() {
        // Arrange
        int id = 1;
        Post post = new Post();
        when(postRepository.getPostById(id)).thenReturn(post);

        // Act
        Post result = postService.getPostById(id);

        // Assert
        assertSame(post, result);
        verify(postRepository, times(1)).getPostById(id);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void createPost_Should_SetUserAndCallRepository() {
        // Arrange
        User user = new User();
        user.setId(5);
        Post post = new Post();
        assertNull(post.getUser());

        // Act
        postService.createPost(post, user);

        // Assert
        assertSame(user, post.getUser());
        verify(postRepository, times(1)).createPost(post);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void updatePost_Should_Update_When_UserIsOwner() {
        // Arrange
        User owner = new User();
        owner.setId(1);
        owner.setAdmin(false);

        Post existing = new Post();
        existing.setId(10);
        existing.setUser(owner);
        existing.setTitle("old");
        existing.setContent("old content");

        Post updateData = new Post();
        updateData.setId(10);
        updateData.setTitle("new title");
        updateData.setContent("new content");

        when(postRepository.getPostById(10)).thenReturn(existing);

        // Act
        postService.updatePost(updateData, owner);

        // Assert: existing post should be modified and passed to updatePost
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).getPostById(10);
        verify(postRepository).updatePost(captor.capture());
        Post updated = captor.getValue();

        assertEquals("new title", updated.getTitle());
        assertEquals("new content", updated.getContent());
        assertSame(owner, updated.getUser());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void updatePost_Should_Update_When_UserIsAdmin() {
        // Arrange
        User owner = new User();
        owner.setId(1);
        owner.setAdmin(false);

        User admin = new User();
        admin.setId(2);
        admin.setAdmin(true);

        Post existing = new Post();
        existing.setId(10);
        existing.setUser(owner);

        Post updateData = new Post();
        updateData.setId(10);
        updateData.setTitle("admin title");
        updateData.setContent("admin content");

        when(postRepository.getPostById(10)).thenReturn(existing);

        // Act
        postService.updatePost(updateData, admin);

        // Assert
        verify(postRepository).getPostById(10);
        verify(postRepository).updatePost(any(Post.class));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void updatePost_Should_Throw_When_UserIsNotOwnerOrAdmin() {
        // Arrange
        User owner = new User();
        owner.setId(1);
        owner.setAdmin(false);

        User other = new User();
        other.setId(2);
        other.setAdmin(false);

        Post existing = new Post();
        existing.setId(10);
        existing.setUser(owner);

        Post updateData = new Post();
        updateData.setId(10);

        when(postRepository.getPostById(10)).thenReturn(existing);

        // Act & Assert
        assertThrows(AuthorizationException.class,
                () -> postService.updatePost(updateData, other));

        verify(postRepository, times(1)).getPostById(10);
        verify(postRepository, never()).updatePost(any());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void deletePostById_Should_Delete_When_UserIsOwner() {
        // Arrange
        User owner = new User();
        owner.setId(1);
        owner.setAdmin(false);

        Post existing = new Post();
        existing.setId(10);
        existing.setUser(owner);

        when(postRepository.getPostById(10)).thenReturn(existing);

        // Act
        postService.deletePostById(10, owner);

        // Assert
        verify(postRepository).getPostById(10);
        verify(postRepository).deletePostById(10);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void deletePostById_Should_Throw_When_UserIsNotOwnerOrAdmin() {
        // Arrange
        User owner = new User();
        owner.setId(1);
        owner.setAdmin(false);

        User other = new User();
        other.setId(2);
        other.setAdmin(false);

        Post existing = new Post();
        existing.setId(10);
        existing.setUser(owner);

        when(postRepository.getPostById(10)).thenReturn(existing);

        // Act & Assert
        assertThrows(AuthorizationException.class,
                () -> postService.deletePostById(10, other));

        verify(postRepository).getPostById(10);
        verify(postRepository, never()).deletePostById(anyInt());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void incrementPostLikes_Should_IncrementAndPersist_When_NotLikedYet() {
        // Arrange
        int postId = 10;
        User user = new User();
        user.setId(3);

        Post post = new Post();
        post.setId(postId);
        post.setLikes(5);

        when(postRepository.checkIfPostIsLikedByUser(postId, user.getId())).thenReturn(false);
        when(postRepository.getPostById(postId)).thenReturn(post);

        // Act
        postService.incrementPostLikes(postId, user);

        // Assert
        assertEquals(6, post.getLikes());
        verify(postRepository).checkIfPostIsLikedByUser(postId, user.getId());
        verify(postRepository).getPostById(postId);
        verify(postRepository).updatePost(post);
        verify(postRepository).addLikeToPost(postId, user.getId());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void incrementPostLikes_Should_Throw_When_AlreadyLiked() {
        // Arrange
        int postId = 10;
        User user = new User();
        user.setId(3);

        when(postRepository.checkIfPostIsLikedByUser(postId, user.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(AuthorizationException.class,
                () -> postService.incrementPostLikes(postId, user));

        verify(postRepository).checkIfPostIsLikedByUser(postId, user.getId());
        verify(postRepository, never()).getPostById(anyInt());
        verify(postRepository, never()).updatePost(any());
        verify(postRepository, never()).addLikeToPost(anyInt(), anyInt());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getAllPostsSorted_Should_DelegateToRepository() {
        // Arrange
        String sortBy = "createdAt";
        String order = "desc";
        List<Post> expected = Collections.singletonList(new Post());
        when(postRepository.getAllPostsSorted(sortBy, order)).thenReturn(expected);

        // Act
        List<Post> result = postService.getAllPostsSorted(sortBy, order);

        // Assert
        assertEquals(expected, result);
        verify(postRepository).getAllPostsSorted(sortBy, order);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getTopCommentedPosts_Should_DelegateToRepository() {
        // Arrange
        int limit = 10;
        List<Post> expected = Collections.singletonList(new Post());
        when(postRepository.getTopCommentedPosts(limit)).thenReturn(expected);

        // Act
        List<Post> result = postService.getTopCommentedPosts(limit);

        // Assert
        assertEquals(expected, result);
        verify(postRepository).getTopCommentedPosts(limit);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getAllPostsPaged_Should_DelegateToRepository() {
        // Arrange
        String sortBy = "createdAt";
        String order = "desc";
        int page = 1;
        int pageSize = 10;
        List<Post> expected = Collections.singletonList(new Post());
        when(postRepository.getAllPostsPaged(sortBy, order, page, pageSize)).thenReturn(expected);

        // Act
        List<Post> result = postService.getAllPostsPaged(sortBy, order, page, pageSize);

        // Assert
        assertEquals(expected, result);
        verify(postRepository).getAllPostsPaged(sortBy, order, page, pageSize);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getUserPostsPaged_Should_DelegateToRepository() {
        // Arrange
        int userId = 5;
        String sortBy = "createdAt";
        String order = "desc";
        int page = 1;
        int pageSize = 10;
        List<Post> expected = Collections.singletonList(new Post());
        when(postRepository.getUserPostsPaged(userId, sortBy, order, page, pageSize))
                .thenReturn(expected);

        // Act
        List<Post> result = postService.getUserPostsPaged(userId, sortBy, order, page, pageSize);

        // Assert
        assertEquals(expected, result);
        verify(postRepository).getUserPostsPaged(userId, sortBy, order, page, pageSize);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getTotalPosts_Should_DelegateToRepository() {
        // Arrange
        long expected = 42L;
        when(postRepository.countPosts()).thenReturn(expected);

        // Act
        long result = postService.getTotalPosts();

        // Assert
        assertEquals(expected, result);
        verify(postRepository).countPosts();
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void getTotalUserPosts_Should_DelegateToRepository() {
        // Arrange
        int userId = 7;
        long expected = 3L;
        when(postRepository.countUserPosts(userId)).thenReturn(expected);

        // Act
        long result = postService.getTotalUserPosts(userId);

        // Assert
        assertEquals(expected, result);
        verify(postRepository).countUserPosts(userId);
        verifyNoMoreInteractions(postRepository);
    }
}
