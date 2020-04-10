package com.simoc.api.services;

import com.simoc.api.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService  {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    User findById(Integer id);
    User add(User user);
    void delete(Integer id);
    User findByUsername(String username);
}
