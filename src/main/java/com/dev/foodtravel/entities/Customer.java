package com.dev.foodtravel.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private Long travels;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public void setOrders(List<Food> orders) {
        this.orders = orders;
    }
}
