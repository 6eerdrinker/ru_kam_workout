package com.example.ru_kam_workout.model;

import lombok.Data;

import java.util.List;

@Data
public class Recipe {

    private String recipeName;

    private int preparingTime;

    private List<Ingredient> ingredients;

    private List<String> cookingSteps;

}
