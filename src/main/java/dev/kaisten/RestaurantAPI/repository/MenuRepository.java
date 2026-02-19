package dev.kaisten.RestaurantAPI.repository;

import dev.kaisten.RestaurantAPI.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
