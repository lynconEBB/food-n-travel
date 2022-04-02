package com.dev.foodtravel.repositories;

import com.dev.foodtravel.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface responsavel por realizar ações no banco de dados relacionadas com a tabela Foods
 * Pode ser considerada como uma DAO (Data Access Object)
 * A implementação desta interface é feita pelo proprio framework Spring
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findAllByActive(boolean active);
}
