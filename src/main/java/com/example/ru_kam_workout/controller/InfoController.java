package com.example.ru_kam_workout.controller;

import com.example.ru_kam_workout.record.InfoRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class InfoController {
    @GetMapping
    public String index() {
        return "Приложение запущено.";
    }

    @GetMapping("/info")
    public InfoRecord info() {
        return new InfoRecord("Александр Клемперт", "Кулинарные рецепты",
                LocalDate.of(2022, 12, 12), "Данное приложение по названию " +
                "рецепта выдает список ингредиентов блюда  и их количество, так же подробно " +
                "описывается пошаговое приготовление блюда.");
    }
}
