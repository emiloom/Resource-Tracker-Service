package com.restinginbed.teamproject.jparepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restinginbed.teamproject.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
