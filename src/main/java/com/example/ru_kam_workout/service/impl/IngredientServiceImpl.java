package com.example.ru_kam_workout.service.impl;

import com.example.ru_kam_workout.model.Ingredient;
import com.example.ru_kam_workout.model.Recipe;
import com.example.ru_kam_workout.service.IngredientService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class IngredientServiceImpl implements IngredientService {

    private final Map<Long, Ingredient> ingredientMap = new HashMap<>();
    private long counter = 0;
    private final Path path;
    private final ObjectMapper objectMapper;


    public IngredientServiceImpl(@Value("${application.file.ingredients}") String path) {
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
            Map<Long, Ingredient> mapFromFile = objectMapper.readValue(file,
                    new TypeReference<>() {
                    });
            ingredientMap.putAll(mapFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Метод для записи данных в файл
    private void writeDataToFile(Map<Long, Ingredient> ingredientMap) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(ingredientMap);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Метод скачивания списка всех рецептов в Json - файле
    @Override
    public InputStreamResource getAllInBytes() {
        try {
            return new InputStreamResource(new FileInputStream(path.toFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Метод добавления ингредиента
    @Override
    public Ingredient addIngredient(Ingredient ingredient) {

            ingredientMap.put(this.counter++, ingredient);
            writeDataToFile(ingredientMap);
            return ingredient;
    }

    //Метод получения ингредиента по идентификатору
    @Override
    public Ingredient getIngredient(long id) {
        if (ingredientMap.containsKey(id)) {
            return ingredientMap.get(id);
        } else {
            throw new RuntimeException("Ингредиент с таким id не найден!");
        }
    }

    //Метод поучения списка ингредиентов
    @Override
    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(this.ingredientMap.values());
    }

    //Метод редактирования ингредиента
    @Override
    public Ingredient updateIngredient(long id, Ingredient ingredient) {
        if (ingredientMap.containsKey(id)) {
           ingredientMap.put(id, ingredient);
           writeDataToFile(ingredientMap);
           return ingredient;
        }
        return null;
    }

    //Метод удаления ингредиента
    @Override
    public Ingredient deleteIngredient(long id) {
        Ingredient ingredient = ingredientMap.remove(id);
        writeDataToFile(ingredientMap);
        return ingredient;
    }

    @Override
    public void importIngredients(MultipartFile ingredients) {
        try {
            Map<Long, Ingredient> mapFromRequest = objectMapper.readValue(ingredients.getBytes(),
                    new TypeReference<>() {
                    });
            writeDataToFile(mapFromRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
