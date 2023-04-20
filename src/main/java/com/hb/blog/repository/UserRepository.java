package com.hb.blog.repository;

import com.hb.blog.model.User;
import com.hb.blog.view.UserView;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from User u where u.id = :id")
    Integer deleteUserById(@Param("id") Long id);

    List<UserView> getUserByFirstNameOrLastName(String firstName, String lastName);
}
