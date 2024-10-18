package com.restinginbed.TeamProject.jpa_repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restinginbed.TeamProject.Organization; 

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
  @Modifying
  @Transactional
  @Query(value = "INSERT INTO \"Organization\" (organization_id, organization_name, organization_location) " +
          "VALUES (:id, :name, :location)", nativeQuery = true)
  void insertOrganization(@Param("id") int id, @Param("name") String name, @Param("location") String location);
}
