package com.simoc.api.repositories;

import com.simoc.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query("select u from User u where u.username=?1")
    User findByUsernameQuery(String username);
}
