package com.example.ru_kam_workout.controller;

import com.example.ru_kam_workout.model.Ingredient;
import com.example.ru_kam_workout.model.Recipe;
import com.example.ru_kam_workout.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Tag(name = "ИНГРЕДИЕНТЫ ДЛЯ КУЛИНАРНЫХ РЕЦЕПТОВ", description = "Возможность добавления нового ингредиента, " +
        "просмотра списка ингредиентов, просмотр, редактирование и удаление ингредиента по идентификатору")

public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @Operation(summary = "СПИСОК ВСЕХ ИНГРЕДИЕНТОВ",
            description = "В списке указаны только добавленные ингредиенты")
    public List<Ingredient> getAllIbgredients() {
        return this.ingredientService.getAllIngredients();
    }

    @PostMapping
    @Operation(
            summary = "ДОБАВЛЕНИЕ НОВОГО ИНГРЕДИЕНТА",
            description = " Ингредиент добавляется формате:  1. Название;  2. Количество;" +
                    "  3. Единицы измерения.")

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новый ингредиент добавлен",
                            content = { @Content(
                                    mediaType = "application/jason",
                                    array = @ArraySchema(schema = @Schema(implementation = Recipe.class)))
                            }
                    )
            }
    )

    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.addIngredient(ingredient);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ПОИСК ИНГРЕДИЕНТА ПО ИДЕНТИФИКАТОРУ",
            description = "Необходимо указать идентификационный номер ингредиента в списке")
    @Parameters(
            value = {
                    @Parameter(name = "id", example = "2"
                    )}
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Необходимый ингредиент найден",
                            content = {@Content(mediaType = "application/jason")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ингредиента с данным идентификатором не найдено!",
                            content = {}
                    )
            }
    )
    public Ingredient getIngredient(@PathVariable("id") long id) {
        return ingredientService.getIngredient(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "ВНЕСЕНИЕ ИЗМЕНЕНИЙ В ИНГРЕДИЕНТЫ",
            description = "Изменение ингредиента, найденного по идентификатору из списка рецептов"
    )
    @Parameters(
            value = {
                    @Parameter (name = "id", example = "2"
                    )}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изменение ингредиента выполнено успешно!",
                            content = {@Content(mediaType = "application/jason")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ингредиента с данным идентификатором не найдено!",
                            content = {}
                    )
            })

    public Ingredient updateIngredient(@PathVariable("id") long id, @RequestBody Ingredient ingredient) {
        return ingredientService.updateIngredient(id, ingredient);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "УДАЛЕНИЕ ИНГРЕДИЕНТА ИЗ СПИСКА",
            description = "Удаление ингредиента, найденного по идентификатору из списка ингредиентов"
    )

    @Parameters(
            value = {
                    @Parameter(name = "id", example = "2"
                    )}
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление выполнено успешно!",
                            content = {@Content(mediaType = "application/jason")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ингредиента с данным идентификатором не найдено!",
                            content = {}
                    )
            })

    public Ingredient deleteIngredient(@PathVariable("id") long id) {
        return ingredientService.deleteIngredient(id);
    }

    @GetMapping("/download")
    @Operation(
            summary = "СКАЧИВАНИЕ ФАЙЛА СО СПИСКОМ ВСЕХ ИНГРЕДИЕНТОВ",
            description = " файл скачивается в Json формате"
    )
    public ResponseEntity<InputStreamResource> downloadRecipes() throws IOException {
        InputStreamResource inputStreamResource = ingredientService.getAllInBytes();
        if (inputStreamResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; fileName=\"ingredients.json\"")
                .body(inputStreamResource);

    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "ВНЕСЕНИЕ ИЗМЕНЕНИЙ ИНГРЕДИЕНТОВ И СОХРАНЕНИЕ ИХ НА ЖЕСТКОМ ДИСКЕ",
            description = "Json-файл с измененным списком ингредиентов и изменениями " +
                    "самих ингредиентов, замещает прежний Json-файл на жестком диске."
    )
    public void importIngredients(MultipartFile ingredients) {
        ingredientService.importIngredients(ingredients);
    }
}
