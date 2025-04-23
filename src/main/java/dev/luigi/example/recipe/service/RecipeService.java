package dev.luigi.example.recipe.service;


import dev.luigi.example.recipe.dto.RecipeRequestDTO;
import dev.luigi.example.recipe.entity.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();

    List<Recipe> findAll(int pageNum, int pageSize);

    Recipe findById(Long id);

    Recipe save(RecipeRequestDTO recipeRequestDTO);

    Recipe update(Long id, RecipeRequestDTO recipeRequestDTO);

    Recipe patchName(Long id, String name);

    void deleteById(Long id);

    void deleteByName(String name);
}
