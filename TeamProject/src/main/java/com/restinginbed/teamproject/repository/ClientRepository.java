package com.restinginbed.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restinginbed.teamproject.model.Client;

/**
 * Repository interface for performing CRUD operations on Client entities.
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
