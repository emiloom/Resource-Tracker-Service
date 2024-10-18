package com.restinginbed.TeamProject.jpa_repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restinginbed.TeamProject.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
