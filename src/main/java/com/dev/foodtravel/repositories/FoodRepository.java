package com.dev.foodtravel.repositories;

import com.dev.foodtravel.entities.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends CrudRepository<Food, Long> { }
