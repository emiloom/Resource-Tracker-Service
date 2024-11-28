package com.restinginbed.teamproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restinginbed.teamproject.model.Organization;

/**
 * Repository interface for performing CRUD operations on Organization entities.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
