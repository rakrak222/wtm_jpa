package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email); // 이메일 중복 확인 메서드

    User findUserById(Long id);

    @Query("select u from User u where u.email = :email and u.social = false")
    Optional<User> getWithRoles(String email);
    
    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.email = :email")
    void updatePassword(@Param("password") String password, @Param("email") String email);
}
