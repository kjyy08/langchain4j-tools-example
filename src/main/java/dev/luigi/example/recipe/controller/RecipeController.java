package dev.luigi.example.recipe.controller;

import dev.luigi.example.recipe.dto.RecipeRequestDTO;
import dev.luigi.example.recipe.entity.Recipe;
import dev.luigi.example.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<?> getAllRecipes() {
        return ResponseEntity.ok().body(recipeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok().body(recipeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> saveRecipe(@RequestBody RecipeRequestDTO recipeRequestDTO) {
        Recipe recipe = recipeService.save(recipeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody RecipeRequestDTO recipeRequestDTO) {
        Recipe recipe = recipeService.update(id, recipeRequestDTO);
        return ResponseEntity.ok().body(recipe);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<?> patchRecipeName(@PathVariable Long id, @RequestBody String name) {
        Recipe recipe = recipeService.patchName(id, name);
        return ResponseEntity.ok().body(recipe);
    }
}
