package com.example.ru_kam_workout.model;

import lombok.Data;

import java.util.List;

@Data
public class Ingredient {

    private  String ingredientName;

    private  int amount;

    private  String unit;

    @Override
    public String toString() {
        return ingredientName + " - " + amount + " " + unit;
    }
}
