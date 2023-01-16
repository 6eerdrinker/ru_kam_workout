package com.example.ru_kam_workout.controller;

import com.example.ru_kam_workout.model.Recipe;
import com.example.ru_kam_workout.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@Tag(name = "КУЛИНАРНЫЕ РЕЦЕПТЫ", description = "Возможность добавления нового рецепта, " +
        "просмотра списка рецептов, просмотр, редактирование и удаление рецепта по идентификатору")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @Operation(summary = "СПИСОК ВСЕХ РЕЦЕПТОВ",
            description = "В списке указаны только добавленные рецепты")

    public List<Recipe> getAllRecipes() {
        return this.recipeService.getAllRecipes();
    }

    @PostMapping
    @Operation(
            summary = "ДОБАВЛЕНИЕ НОВОГО РЕЦЕПТА",
            description = " Рецепт добавляется формате:  1. Название;  2. Время приготовления;" +
                    "  3. Список и количество ингредиентов;  4. Шаги приготовления.")

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новый рецепт добавлен",
                            content = {@Content(
                                    mediaType = "application/jason",
                                    array = @ArraySchema(schema = @Schema(implementation = Recipe.class)))
                            }
                    )
            }
    )
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        if (StringUtils.isBlank(recipe.getRecipeName())) {
            return ResponseEntity.badRequest().body("Не введено название рецепта!");
        }

        return ResponseEntity.ok(recipeService.addRecipe(recipe));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ПОИСК РЕЦЕПТА ПО ИДЕНТИФИКАТОРУ",
            description = "Необходимо указать идентификационный номер рецепта в списке")
    @Parameters(
            value = {
                    @Parameter(name = "id", example = "2"
                    )}
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Необходимый рецепт найден",
                            content = {@Content(mediaType = "application/jason")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Рецепта с данным идентификатором не найдено!",
                            content = {}
                    )
            }
    )

    public Recipe getRecipe(@PathVariable("id") long id) {
        return recipeService.getRecipe(id);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "ВНЕСЕНИЕ ИЗМЕНЕНИЙ В РЕЦЕПТЫ",
            description = "Изменение рецепта, найденного по идентификатору из списка рецептов"
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
                            description = "Изменение рецепта выполнено успешно!",
                            content = {@Content(mediaType = "application/jason")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Рецепта с данным идентификатором не найдено!",
                            content = {}
                    )
            })

    public Recipe updateRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "УДАЛЕНИЕ РЕЦЕПТА ИЗ СПИСКА",
            description = "Удаление рецепта, найденного по идентификатору из списка рецептов"
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
                            description = "Удаление рецепта выполнено успешно!",
                            content = {@Content(mediaType = "application/jason")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Рецепта с данным идентификатором не найдено!",
                            content = {}
                    )
            })

    public Recipe deleteRecipe(@PathVariable("id") long id) {
        return recipeService.deleteRecipe(id);
    }

    @GetMapping("/download")
    @Operation(
            summary = "СКАЧИВАНИЕ ФАЙЛА СО СПИСКОМ ВСЕХ РЕЦЕПТОВ",
            description = " файл скачивается в Json формате"
    )
    public ResponseEntity<byte[]> downloadRecipes() {
        byte[] bytes = recipeService.getAllInBytes();
        if (bytes == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; fileName=\"recipes.json\"")
                .body(bytes);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "ВНЕСЕНИЕ ИЗМЕНЕНИЙ В РЕЦЕПТАХ И СОХРАНЕНИЕ ИХ НА ЖЕСТКОМ ДИСКЕ",
            description = "Json-файл с измененным списком рецептов и изменениями " +
                    "в самих рецептах, замещает прежний Json-файл на жестком диске."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Изменения в рецептах выполнены успешно!",
                            content = {@Content(mediaType = "application/jason")
                            })
            })

    public void importRecipes(MultipartFile recipes) {
        recipeService.importRecipes(recipes);
    }

    @GetMapping("/export")
    @Operation(
            summary = "СКАЧИВАНИЕ ВСЕХ РЕЦЕПТОВ",
            description = "Все рецепты описаны в текстовом формате в одном файле"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Скачивание рецептов выполнено успешно!",
                            content = {@Content(mediaType = "application/txt")}),
                    @ApiResponse(responseCode = "500",
                    description = "Во время выполнения запроса произошла ошибка на сервере!")

            })
    public ResponseEntity<byte[]> exportTxt() {

        byte[] bytes = recipeService.exportTxt();
        if (bytes == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; fileName=\"info.txt\"")
                .body(bytes);
    }
}
