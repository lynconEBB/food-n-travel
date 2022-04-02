package com.dev.foodtravel.repositories;

import com.dev.foodtravel.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Responsavel por realizar ações no banco de dados relacionadas com a tabela Customers
 * Pode ser considerada como uma DAO (Data Access Object)
 * A implementação desta interface é feita pelo proprio framework Spring
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findAll();
}
