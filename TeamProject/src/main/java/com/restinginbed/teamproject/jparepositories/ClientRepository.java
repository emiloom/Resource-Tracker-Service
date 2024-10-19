package com.restinginbed.teamproject.jparepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restinginbed.teamproject.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
