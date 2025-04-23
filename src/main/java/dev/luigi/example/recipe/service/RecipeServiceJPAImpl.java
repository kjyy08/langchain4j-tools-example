package dev.luigi.example.recipe.service;

import dev.luigi.example.common.exception.CustomException;
import dev.luigi.example.recipe.dto.RecipeRequestDTO;
import dev.luigi.example.recipe.entity.Recipe;
import dev.luigi.example.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceJPAImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public List<Recipe> findAll(int pageNum, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return recipeRepository.findAll(pageable).getContent();
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new CustomException("레시피를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    @Override
    public Recipe save(RecipeRequestDTO recipeRequestDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeRequestDTO.getName());
        recipe.setDescription(recipeRequestDTO.getDescription());
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Long id, RecipeRequestDTO recipeRequestDTO) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new CustomException("레시피를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        recipe.setName(recipeRequestDTO.getName());
        recipe.setDescription(recipeRequestDTO.getDescription());
        return recipe;
    }

    @Override
    @Transactional
    public Recipe patchName(Long id, String name) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new CustomException("레시피를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        recipe.setName(name);
        return recipe;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional
    public void deleteByName(String name){
        recipeRepository.deleteByName(name);
    }
}
