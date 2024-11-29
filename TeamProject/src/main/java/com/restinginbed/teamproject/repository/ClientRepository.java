package com.restinginbed.teamproject.repository;

import com.restinginbed.teamproject.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on Client entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
