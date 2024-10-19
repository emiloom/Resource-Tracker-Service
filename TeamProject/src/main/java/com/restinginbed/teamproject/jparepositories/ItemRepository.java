package com.restinginbed.teamproject.jparepositories;

import com.restinginbed.teamproject.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Item entities.
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
  List<Item> findByNameContaining(String name);

  List<Item> findByOrganizationId(Integer organizationId);
}
