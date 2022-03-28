package com.dev.foodtravel.controllers;

import com.dev.foodtravel.entities.Food;
import com.dev.foodtravel.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/foods")
public class FoodController {
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
    public String createOrUpdate(@ModelAttribute("food") Food food) {
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
