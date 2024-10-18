package com.restinginbed.TeamProject.jpa_repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restinginbed.TeamProject.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
