package com.example.forum.services;

import com.example.forum.repositories.CommentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {
    @Mock
    CommentRepository mockRepository;

    @InjectMocks
    CommentServiceImpl service;
}
