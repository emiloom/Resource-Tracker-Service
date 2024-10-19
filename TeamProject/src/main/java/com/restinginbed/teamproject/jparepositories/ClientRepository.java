package com.restinginbed.teamproject.jparepositories;

import com.restinginbed.teamproject.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Client entities.
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
