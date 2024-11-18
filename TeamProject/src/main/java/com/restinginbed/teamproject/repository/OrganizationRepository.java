package com.restinginbed.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restinginbed.teamproject.model.Organization;

/**
 * Repository interface for performing CRUD operations on Organization entities.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
