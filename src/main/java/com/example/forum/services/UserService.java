package com.example.forum.services;

import com.example.forum.models.User;

import java.util.List;

public interface UserService {

    List<User> get();

    User get(int id);

    //Possible solution for AuthHelpers
    //User get(String email);

    User get (String username);

    void create (User user);
}
