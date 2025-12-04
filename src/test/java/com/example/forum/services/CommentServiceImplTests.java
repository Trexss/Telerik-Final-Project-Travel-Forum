package com.example.forum.services;

import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {
    @Mock
    CommentRepository mockRepository;
    @Mock
    PostService postService;

    @InjectMocks
    CommentServiceImpl service;

    @Test
    void getCommentsByPostId_Should_Return_Comments_From_PostService() {
        // Arrange
        int postId = 1;
        Post post = new Post();
        Comment comment = new Comment();
        post.addComment(comment);
        when(postService.getPostById(postId)).thenReturn(post);

        // Act
        List<Comment> result = service.getCommentsByPostId(postId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(comment));
        verify(postService, times(1)).getPostById(postId);
        verifyNoInteractions(mockRepository);
    }
    @Test
    void getCommentsByPostId_Should_ThrowException_When_NoCommentsExist() {
        // Arrange
        int postId = 1;
        Post post = new Post();
        when(postService.getPostById(postId)).thenReturn(post);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.getCommentsByPostId(postId));
        verify(postService, times(1)).getPostById(postId);
        verifyNoInteractions(mockRepository);
    }
    @Test
    void getCommentsByPostId_Should_ThrowException_When_PostNotFound() {
        // Arrange
        int postId = 1;
        when(postService.getPostById(postId)).thenThrow(new com.example.forum.exceptions.EntityNotFoundException("Post", "id", String.valueOf(postId)));

        // Act & Assert
        assertThrows(com.example.forum.exceptions.EntityNotFoundException.class,
                () -> service.getCommentsByPostId(postId));

        verify(postService, times(1)).getPostById(postId);
        verifyNoInteractions(mockRepository);
    }
    @Test
    void addCommentToPost_Should_Throw_When_UserIsBlocked() {
        // Arrange
        int postId = 1;
        Comment comment = new Comment();
        User mockUser = Mockito.mock(com.example.forum.models.User.class);
        when(mockUser.isBlocked()).thenReturn(true);

        // Act & Assert
        assertThrows(com.example.forum.exceptions.AuthorizationException.class,
                () -> service.addCommentToPost(postId, comment, mockUser));

        verifyNoInteractions(postService);
        verifyNoInteractions(mockRepository);
    }
    @Test
    void addCommentToPost_Should_SaveComment_When_UserIsNotBlocked() {
        // Arrange
        int postId = 1;
        Comment comment = new Comment();
        User mockUser = Mockito.mock(com.example.forum.models.User.class);
        Post mockPost = Mockito.mock(Post.class);
        when(mockUser.isBlocked()).thenReturn(false);
        when(postService.getPostById(postId)).thenReturn(mockPost);
        when(mockRepository.save(comment)).thenReturn(comment);

        // Act
        Comment result = service.addCommentToPost(postId, comment, mockUser);

        // Assert
        assertEquals(comment, result);
        assertEquals(mockPost, comment.getPost());
        assertEquals(mockUser, comment.getUser());
        verify(postService, times(1)).getPostById(postId);
        verify(mockRepository, times(1)).save(comment);
    }
    @Test
    void getCommentById_Should_Return_Comment_From_Repository() {
        // Arrange
        int commentId = 1;
        Comment mockComment = Mockito.mock(Comment.class);
        when(mockRepository.getCommentById(commentId)).thenReturn(mockComment);

        // Act
        Comment result = service.getCommentById(commentId);

        // Assert
        assertEquals(mockComment, result);
        verify(mockRepository, times(1)).getCommentById(commentId);
    }
    @Test
    void getCommentById_Should_ThrowException_When_CommentNotFound() {
        // Arrange
        int commentId = 1;
        when(mockRepository.getCommentById(commentId)).thenThrow(new com.example.forum.exceptions.EntityNotFoundException("Comment", "id", String.valueOf(commentId)));

        // Act & Assert
        assertThrows(com.example.forum.exceptions.EntityNotFoundException.class,
                () -> service.getCommentById(commentId));

        verify(mockRepository, times(1)).getCommentById(commentId);
    }
    @Test
    void updateComment_Should_Update_Comment_In_Repository_WhenUserIsAdmin() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1);
        User user = new User();
        user.setAdmin(true);
        comment.setUser(user);
        when(mockRepository.getCommentById(1)).thenReturn(comment);

        // Act
        service.updateComment(1, comment, user);

        // Assert
        verify(mockRepository, times(1)).save(comment);
    }
    @Test
    void updateComment_Should_Update_Comment_In_Repository_WhenUserIsCreator() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1);
        User user = new User();
        user.setId(1);
        comment.setUser(user);

        when(mockRepository.getCommentById(1)).thenReturn(comment);

        // Act
        service.updateComment(1, comment, user);

        // Assert
        verify(mockRepository, times(1)).save(comment);
    }
    @Test
    void updateComment_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1);
        User creator = new User();
        creator.setId(1);
        comment.setUser(creator);

        User otherUser = new User();
        otherUser.setId(2);

        when(mockRepository.getCommentById(1)).thenReturn(comment);

        // Act & Assert
        assertThrows(com.example.forum.exceptions.AuthorizationException.class,
                () -> service.updateComment(1, comment, otherUser));

        verify(mockRepository, never()).save(comment);
    }
    @Test
    void updateComment_Should_ThrowException_When_CommentNotFound() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1);
        User user = new User();
        user.setAdmin(true);

        when(mockRepository.getCommentById(1)).thenThrow(new com.example.forum.exceptions.EntityNotFoundException("Comment", "id", "1"));

        // Act & Assert
        assertThrows(com.example.forum.exceptions.EntityNotFoundException.class,
                () -> service.updateComment(1, comment, user));

        verify(mockRepository, never()).save(comment);
    }
    @Test
    void deleteComment_Should_Delete_Comment_From_Repository_WhenUserIsAdmin() {
        // Arrange
        int commentId = 1;
        Comment mockComment = new Comment();
        User mockUser = new User();
        mockUser.setAdmin(true);
        mockComment.setUser(mockUser);

        when(mockRepository.getCommentById(commentId)).thenReturn(mockComment);

        // Act
        service.deleteComment(commentId, mockUser);

        // Assert
        verify(mockRepository, times(1)).deleteById(commentId);
    }
    @Test
    void deleteComment_Should_Delete_Comment_From_Repository_WhenUserIsCreator() {
        // Arrange
        int commentId = 1;
        Comment mockComment = new Comment();
        User mockUser = new User();
        mockUser.setId(1);
        mockComment.setUser(mockUser);

        when(mockRepository.getCommentById(commentId)).thenReturn(mockComment);

        // Act
        service.deleteComment(commentId, mockUser);

        // Assert
        verify(mockRepository, times(1)).deleteById(commentId);
    }
    @Test
    void deleteComment_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        int commentId = 1;
        Comment mockComment = new Comment();
        User creator = new User();
        creator.setId(1);
        mockComment.setUser(creator);

        User otherUser = new User();
        otherUser.setId(2);

        when(mockRepository.getCommentById(commentId)).thenReturn(mockComment);

        // Act & Assert
        assertThrows(com.example.forum.exceptions.AuthorizationException.class,
                () -> service.deleteComment(commentId, otherUser));

        verify(mockRepository, never()).deleteById(commentId);
    }
    @Test
    void deleteComment_Should_ThrowException_When_CommentNotFound() {
        // Arrange
        int commentId = 1;
        User mockUser = new User();
        mockUser.setAdmin(true);

        when(mockRepository.getCommentById(commentId)).thenThrow(new com.example.forum.exceptions.EntityNotFoundException("Comment", "id", String.valueOf(commentId)));

        // Act & Assert
        assertThrows(com.example.forum.exceptions.EntityNotFoundException.class,
                () -> service.deleteComment(commentId, mockUser));

        verify(mockRepository, never()).deleteById(commentId);
    }

}
