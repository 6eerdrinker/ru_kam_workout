package com.example.ru_kam_workout.service.impl;

import com.example.ru_kam_workout.model.Recipe;
import com.example.ru_kam_workout.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final Map<Long, Recipe> recipeMap = new HashMap<>();
    private long counter = 0L;
    private final Path path;
    private final ObjectMapper objectMapper;

    public RecipeServiceImpl(@Value("${application.file.recipes}") String path) {
        try {
            this.path = Paths.get(path);
            this.objectMapper = new ObjectMapper();
        } catch (InvalidPathException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Загрузка данных из файла при старте приложения
    @PostConstruct
    private void init() {
        readDataFromFile();
    }

    //Метод считывания данных из файла
    private void readDataFromFile() {
        try {
            byte[] file = Files.readAllBytes(path);
            Map<Long, Recipe> mapFromFile = objectMapper.readValue(file,
                    new TypeReference<>() {
                    });
            recipeMap.putAll(mapFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Метод для записи данных в файл
    private void writeDataToFile(Map<Long, Recipe> recipeMap) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(recipeMap);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Метод добавления рецепта
    @Override
    public Recipe addRecipe(Recipe recipe) {
        recipeMap.put(this.counter++, recipe);
        writeDataToFile(recipeMap);
        return recipe;
    }

    //Метод получения рецепта по идентификатору
    @Override
    public Recipe getRecipe(long id) {
        if (recipeMap.containsKey(id)) {
            return recipeMap.get(id);
        } else {
            throw new RuntimeException("Рецепт с таким id не найден!");
        }
    }

    //Метод получения списка рецептов
    @Override
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(this.recipeMap.values());
    }

    //Метод редактирования рецепта
    @Override
    public Recipe updateRecipe(long id, Recipe recipe) {
        if (recipeMap.containsKey(id)) {
            recipeMap.put(id, recipe);
            writeDataToFile(recipeMap);
            return recipe;
        }
        return null;
    }
    //Метод удаления рецепта
    @Override
    public Recipe deleteRecipe(long id) {
        Recipe recipe = recipeMap.remove(id);
        writeDataToFile(recipeMap);
        return recipe;
    }

    //Метод скачивания всех рецептов в виде Json-файла
    @Override
    public byte[] getAllInBytes() {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void importRecipes(MultipartFile recipes) {
        try {
            Map<Long, Recipe> mapFromRequest = objectMapper.readValue(recipes.getBytes(),
                    new TypeReference<>() {
                    });
            writeDataToFile(mapFromRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
