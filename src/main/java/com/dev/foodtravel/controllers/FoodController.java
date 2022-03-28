package com.dev.foodtravel.controllers;

import com.dev.foodtravel.entities.Food;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/foods")
public class FoodController {

    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);
    private final FoodRepository repository;

    @Autowired
    public FoodController(FoodRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String findAll(Model model) {
        Iterable<Food> foods = repository.findAll();
        model.addAttribute("foods", foods);
        return "foodsList";
    }

    @PostMapping
    public String createOrUpdate(@Valid @ModelAttribute("food") Food food, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                if(food.getId() != null)
                    logger.error("Erro durante atualização do alimento de id {}, {}",food.getId(),error.getDefaultMessage());
                else
                    logger.error("Erro durante cadastro de novo alimento, {}",error.getDefaultMessage());
            }
            return "foodsForm";
        }
        repository.save(food);
        return "redirect:/foods";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Optional<Food> food = repository.findById(id);
        if (food.isPresent()) {
            food.get().setActive(false);
            repository.save(food.get());
            return "redirect:/foods";
        }
        logger.error("Alimento com id {} não encontrado para executar remoção", id);
        return "/notFind";
    }

    @GetMapping("/form")
    public String showCreateForm(@RequestParam(name="id", required = false) Long id, Model model) {
        Food food = new Food();
        boolean update = false;

        if(id != null) {
           Optional<Food> optionalFood = repository.findById(id);
           if (optionalFood.isEmpty())
               return "notFind";
           update = true;
           food = optionalFood.get();
        }
        model.addAttribute("update", update);
        model.addAttribute("food", food);
        return "foodsForm";
    }
}
