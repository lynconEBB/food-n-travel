package com.dev.foodtravel.entities;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Campo nome do alimento não pode ser vazio")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Campo preço não pode ser vazio")
    @Positive(message = "Valor calorico precisa ser maior que 0")
    @Column(nullable = false)
    private Double caloricValue;

    @NotNull(message = "Campo preço não pode ser vazio")
    @Positive(message = "Preço deve ser maior que 0")
    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private String brand;

    private Boolean active = true;

    public Food() {}

    @PreUpdate
    @PrePersist
    public void preInsert() {
        if (this.brand == null || this.brand.equals("")) {
            this.brand = "Sem marca";
        }
    }

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
