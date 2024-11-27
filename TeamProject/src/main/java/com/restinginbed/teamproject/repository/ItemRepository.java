package com.restinginbed.teamproject.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restinginbed.teamproject.model.Item;

/**
 * Repository interface for performing CRUD operations on Item entities.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
  List<Item> findByNameContaining(String name);

  List<Item> findByOrganizationId(Integer organizationId);
}
