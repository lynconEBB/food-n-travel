package com.dev.foodtravel.entities;

import javax.persistence.*;

@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double caloricValue;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private String brand = "Sem marca";

    private Boolean active = true;

    public Food() {}

    public Food(String name, Double caloricValue, Double price) {
        this.name = name;
        this.caloricValue = caloricValue;
        this.price = price;
    }

    public Food(String name, Double caloricValue, Double price, String brand) {
        this.name = name;
        this.caloricValue = caloricValue;
        this.price = price;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCaloricValue() {
        return caloricValue;
    }

    public void setCaloricValue(Double caloricValue) {
        this.caloricValue = caloricValue;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
