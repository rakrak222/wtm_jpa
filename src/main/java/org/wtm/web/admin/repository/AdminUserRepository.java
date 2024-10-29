package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.user.model.User;

public interface AdminUserRepository extends JpaRepository<User, Long> {

}
