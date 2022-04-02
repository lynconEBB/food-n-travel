package com.dev.foodtravel.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe model representando o cliente, está classe é utitlizada como base para
 * a criação das tabelas no banco de dados
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Campo nome completo não pode ser vazaio")
    String name;

    @Past(message = "Data futura inválida")
    @NotNull(message = "Campo data de nascimento não pode ser vazio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @NotNull(message = "Campo numero de viagens não pode ser vazio")
    @PositiveOrZero(message = "Numero de viagens não pode ser negativo")
    private Long travels;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "customer_foods",
                joinColumns = { @JoinColumn(name = "customer_id")},
                inverseJoinColumns = { @JoinColumn(name = "food_id")})
    List<Food> orders = new ArrayList<>();

    public Customer() {}

    public Customer(String name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Customer(String name, Date birthDate, Long travels) {
        this.name = name;
        this.birthDate = birthDate;
        this.travels = travels;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Long getTravels() {
        return travels;
    }

    public void setTravels(Long travels) {
        this.travels = travels;
    }

    public List<Food> getOrders() {
        return orders;
    }

    public List<Food> getActiveOrders() {
        return orders
                .stream()
                .filter(Food::getActive)
                .toList();
    }

    public void setOrders(List<Food> orders) {
        this.orders = orders;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        stringBuilder.append("=================================\n");
        stringBuilder.append("Cliente com id ").append(this.getId()).append('\n')
                .append("Nome: ").append(this.getName()).append('\n')
                .append("Data de Nascimento: ").append(formatter.format(getBirthDate())).append('\n')
                .append("Número de viagens: ").append(getTravels()).append('\n')
                .append("===========Pedidos==============\n");

        if (getActiveOrders().size() == 0)
            stringBuilder.append("Nenhum pedido realizado pelo cliente\n\n");

        getActiveOrders().forEach(food -> {
            stringBuilder.append("Alimento com id ").append(food.getId()).append('\n')
                    .append("Nome: ").append(food.getName()).append('\n')
                    .append("Valor calórico: ").append(food.getCaloricValue()).append('\n')
                    .append("Preço: ").append(food.getPrice()).append('\n')
                    .append("Marca: ").append(food.getBrand()).append("\n\n");
        });
        return stringBuilder.toString();
    }
}