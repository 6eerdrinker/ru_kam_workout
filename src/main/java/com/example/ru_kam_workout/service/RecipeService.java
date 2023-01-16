package com.example.ru_kam_workout.service;

import com.example.ru_kam_workout.model.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {

    //Метод добавления рецепта
    Recipe addRecipe(Recipe recipe);

    //Метод получения рецепта по идентификатору
    Recipe getRecipe(long id);

    //Метод получения списка рецептов
    List<Recipe> getAllRecipes();

    //Метод редактирования рецепта
    Recipe updateRecipe(long id, Recipe recipe);

    //Метод удаления рецепта
    Recipe deleteRecipe(long id);

    byte[] getAllInBytes();

    void importRecipes(MultipartFile recipes);

    byte[] exportTxt();
}


