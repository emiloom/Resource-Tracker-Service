package com.restinginbed.teamproject.jparepositories;

import com.restinginbed.teamproject.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Organization entities.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
