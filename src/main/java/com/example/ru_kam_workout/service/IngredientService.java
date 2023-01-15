package com.example.ru_kam_workout.service;

import com.example.ru_kam_workout.model.Ingredient;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IngredientService {


    //Метод скачивания списка всех рецептов в Json - файле
     InputStreamResource getAllInBytes();

    //Метод добавления ингредиента
    Ingredient addIngredient(Ingredient ingredient);

    //Метод получения ингредиента по идентификатору
    Ingredient getIngredient(long id);

    //Метод поучения списка ингредиентов
    List<Ingredient> getAllIngredients();

    //Метод редактирования ингредиента
    Ingredient updateIngredient(long id, Ingredient ingredient);

    //Метод удаления ингредиента
    Ingredient deleteIngredient(long id);

    void importIngredients(MultipartFile ingredients);
}

