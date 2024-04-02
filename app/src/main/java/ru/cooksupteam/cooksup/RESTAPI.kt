package ru.cooksupteam.cooksup

import ru.cooksupteam.cooksup.app.ivm
import ru.cooksupteam.cooksup.app.rvm
import ru.cooksupteam.cooksup.model.Ingredient
import ru.cooksupteam.cooksup.model.Recipe

object RESTAPI {
    fun fetchRecipeFilteredFromText(name: String): List<Recipe> {
        return rvm.allRecipes.filter {
            it.name.contains(name)
        }
    }

    fun fetchRecipeFiltered(listIngredient: List<Ingredient>): List<Recipe> {
        val list = mutableListOf<Recipe>()
        val listRecipeOther = mutableListOf<Recipe>()
        val listRecipeOtherWithBase = mutableListOf<Recipe>()

        val listIngredientsWithBase = listIngredient.toMutableList()
        val listIngredientsWithBaseString = mutableListOf<String>()

        listIngredient.forEach {
            listIngredientsWithBaseString.add(it.name)
        }
        ivm.allIngredients.filter { it.tags.contains("база") }.forEach {
            listIngredientsWithBaseString.add(it.name)
        }

        filterRecipes(listIngredientsWithBaseString).forEach {
            list.add(it)
        }

        generateVariations(listIngredient).forEach { variation ->
            if (variation.size > listIngredient.size - 4) {
                rvm.allRecipes.filter { recipe ->
                    recipe.ingredients.map(Ingredient::name).containsAll(variation.map { it.name })
                }.forEach {
                    listRecipeOther.add(it)
                }
            }
        }

        generateVariations(listIngredientsWithBase).forEach { variation ->
            if (variation.size > listIngredientsWithBase.size - 4) {
                rvm.allRecipes.filter { recipe ->
                    recipe.ingredients.map(Ingredient::name).containsAll(variation.map { it.name })
                }.forEach {
                    listRecipeOtherWithBase.add(it)
                }
            }
        }

        val ingredientsListString = mutableListOf<String>()
        listIngredient.forEach {
            ingredientsListString.add(it.name)
        }
        list.addAll(sortRecipesBySimilarity(listRecipeOther, ingredientsListString))
        list.addAll(sortRecipesBySimilarity(listRecipeOtherWithBase, ingredientsListString))
        return sortRecipesBySimilarity(list, ingredientsListString).distinct()
    }

    fun similarity(selectedIngredients: List<String>, recipeIngredients: List<Ingredient>): Int {
        return selectedIngredients.intersect(recipeIngredients).count()
    }

    fun sortRecipesBySimilarity(
        recipes: List<Recipe>,
        selectedIngredients: List<String>
    ): List<Recipe> {
        return recipes.sortedByDescending { recipe ->
            similarity(selectedIngredients, recipe.ingredients)
        }
    }

    private fun filterRecipes(selectedIngredients: MutableList<String>): List<Recipe> {
        return rvm.allRecipes.filter { recipe ->
            recipe.ingredients.map { it.name }.containsAll(selectedIngredients)
        }
    }

    fun generateVariations(ingredients: List<Ingredient>): List<List<Ingredient>> {
        val variations = mutableListOf<List<Ingredient>>()
        if (ingredients.isNotEmpty()) {
            for (i in ingredients.indices) {
                val variation = mutableListOf<Ingredient>()
                for (j in i until (i + ingredients.size)) {
                    val index = j % ingredients.size
                    variation.add(ingredients[index])
                    variations.add(variation.toList())
                }
            }
        }

        return variations.sortedBy { it.size }.reversed()
    }
}

