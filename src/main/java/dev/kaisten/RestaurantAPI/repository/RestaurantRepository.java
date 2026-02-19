package dev.kaisten.RestaurantAPI.repository;

import dev.kaisten.RestaurantAPI.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
