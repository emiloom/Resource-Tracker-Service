package com.restinginbed.teamproject.jparepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restinginbed.teamproject.Item;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	List<Item> findByNameContaining(String name);
  List<Item> findByOrganizationId(Integer organizationId);
}
