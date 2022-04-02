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
import java.util.List;
import java.util.Optional;

/**
 * Classe controladora responsavel por lidar com as requisições iniciddas com "/customers/
 * recebidas e garantir uma resposta, seja por meio de uma página html (view) ou outro tipo de arquivo
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository repository;
    private final FoodRepository foodRepository;

    /**
     * Construtor com injeção de dependencia causada pelo framework
     */
    @Autowired
    public CustomerController(CustomerRepository repository, FoodRepository foodRepository) {
        this.repository = repository;
        this.foodRepository = foodRepository;
    }

    /**
     * Metódo acionado ao relizar uma requisição GET para "/customers"
     * Busca todos os clientes do banco utilizando os repositórios e
     * retorna a página contendo esses clientes
     */
    @GetMapping
    public String findAll(Model model) {
        List<Customer> customers = repository.findAll();

        model.addAttribute("customers",customers);
        return "customersList";
    }

    /**
     * Metódo acionado ao relizar uma requisição GET para "/customers/{id}" onde id é o id do cliente
     * Busca e rxibe os detalhes do cliente
     */
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        Optional<Customer> optCustomer = repository.findById(id);

        if (optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            List<Food> foods = foodRepository.findAllByActive(true);
            foods.removeAll(customer.getOrders());

            model.addAttribute("selectedFood", new Food());
            model.addAttribute("customer",customer);
            model.addAttribute("currentHistory", customer.getActiveOrders());
            model.addAttribute("foods",foods);
            return "customerDetails";
        }
        logger.error("Cliente com id {} não encontrado", id);
        return "notFind";
    }

    /**
     * Metódo acionado ao relizar uma requisição GET para "/customers/delete/{id}" onde id é o id do cliente
     * Exclui um cliente do banco de dados caso esse cliente seja encontrado, caso contrario uma pagina de erro é retornada
     */
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

    /**
     * Metódo acionado ao relizar uma requisição POST para "/customers/{id}/addFood" onde id é o id do cliente
     * Adiciona o alimento passado como parametro ao cliente de id passado no caminha da requisição,
     * caso algum destes não seja encontrado uma pagina de erro é retornada
     */
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

    /**
     * Metódo acionado ao relizar uma requisição POST para "/customers"
     * Insere um novo cliente ou atualiza caso um id  valido seja passsado, caso algum campo não seja valido
     * o usuário permanece no formulário com erros indicando as ações a serem tomadas
     */
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

    /**
     * Metódo acionado ao relizar uma requisição GET para "/customers/form"
     * Exibe o formulario de cadastro/atualização
     */
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