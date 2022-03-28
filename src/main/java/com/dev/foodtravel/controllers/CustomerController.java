package com.dev.foodtravel.controllers;

import com.dev.foodtravel.entities.Customer;
import com.dev.foodtravel.entities.Food;
import com.dev.foodtravel.repositories.CustomerRepository;
import com.dev.foodtravel.repositories.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository repository;
    private final FoodRepository foodRepository;

    @Autowired
    public CustomerController(CustomerRepository repository, FoodRepository foodRepository) {
        this.repository = repository;
        this.foodRepository = foodRepository;
    }

    @GetMapping
    public String findAll(Model model) {
        Iterable<Customer> customers = repository.findAll();
        model.addAttribute("customers",customers);
        return "customersList";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        Optional<Customer> optCustomer = repository.findById(id);

        if (optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            List<Food> foods = foodRepository.findAllByActive(true);
            List<Food> notRelatedFoods = new ArrayList<>(foods);
            notRelatedFoods.removeAll(customer.getOrders());

            model.addAttribute("selectedFood", new Food());
            model.addAttribute("customer",customer);
            model.addAttribute("foods",notRelatedFoods);
            return "customerDetails";
        }
        logger.error("Cliente com id {} não encontrado", id);
        return "notFind";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isPresent()) {
            repository.delete(customer.get());
            return "redirect:/customers";
        }
        logger.error("Cliente com id {} não encontrado", id);
        return "/notFind";
    }

    @PostMapping("/{id}/addFood")
    public String addFood(@PathVariable Long id, @ModelAttribute("selectedFood") Food food) {
        Optional<Customer> optionalCustomer = repository.findById(id);
        Optional<Food> optionalFood = foodRepository.findById(food.getId());

        if (optionalCustomer.isPresent() && optionalFood.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.getOrders().add(optionalFood.get());
            repository.save(customer);
            return "redirect:/customers/" + id;
        }
        return "notFind";
    }

    @PostMapping
    public String createOrUpdate(@Valid @ModelAttribute("customer") Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                if (customer.getId() != null)
                    logger.error("Erro durante atualização do cliente de id {}, {}", customer.getId(), error.getDefaultMessage());
                else
                    logger.error("Erro durante cadastro de um novo cliente, {}", error.getDefaultMessage());
            }
            return "customersForm";
        }
        if (customer.getId() != null) {
           Optional<Customer> optionalCustomer = repository.findById(customer.getId());
           customer.setOrders(optionalCustomer.get().getOrders());
        }
        repository.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(name="id", required = false) Long id, Model model) {
        Customer customer = new Customer();
        boolean update = false;
        if(id != null) {
            Optional<Customer> optionalCustomer = repository.findById(id);
            if (optionalCustomer.isEmpty())
                return "notFind";
            update = true;
            customer = optionalCustomer.get();
        }
        model.addAttribute("update", update);
        model.addAttribute("customer", customer);
        return "customersForm";
    }
}
