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

/**
 * Classe controladora responsavel por lidar com as requisições iniciddas com "/foods
 * recebidas e garantir uma resposta, seja por meio de uma página html (view) ou outro tipo de arquivo
 */
@Controller
@RequestMapping("/foods")
public class FoodController {

    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);
    private final FoodRepository repository;

    /**
     * Construtor com injeção de dependencia causada pelo framework
     */
    @Autowired
    public FoodController(FoodRepository repository) {
        this.repository = repository;
    }

    /**
     * Metódo acionado ao relizar uma requisição GET para "/foods"
     * Busca todos os alimentos do banco utilizando os repositórios e retorna a página contendo esses
     * alimentos separados por ativos e excluidos
     */
    @GetMapping
    public String findAll(Model model) {
        List<Food> foods = repository.findAllByActive(true);
        List<Food> excludedFoods = repository.findAllByActive(false);
        model.addAttribute("foods", foods);
        model.addAttribute("excludedFoods", excludedFoods);
        return "foodsList";
    }

    /**
     * Metódo acionado ao relizar uma requisição POST para "/foods"
     * Insere um novo alimento ou atualiza caso um id  valido seja passsado, caso algum campo não seja valido
     * o usuário permanece no formulário com erros indicando as correções a serem realizadas
     */
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

    /**
     * Metódo acionado ao relizar uma requisição GET para "/foods/{id}" onde id é o id do alimento
     * Exclui um alimento do banco alterando seu atributo "active" para false caso seja encontrado,
     * caso contrario uma pagina de erro é retornada
     */
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

    /**
     * Metódo acionado ao relizar uma requisição GET para "/foods/form"
     * Exibe o formulario de cadastro/atualização de um alimento
     */
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
