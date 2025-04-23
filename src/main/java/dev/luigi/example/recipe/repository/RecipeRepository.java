package dev.luigi.example.recipe.repository;

import dev.luigi.example.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    void deleteByName(String name);
}
