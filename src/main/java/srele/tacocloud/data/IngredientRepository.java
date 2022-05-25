package srele.tacocloud.data;

import srele.tacocloud.Ingredient;

public interface IngredientRepository {

    Iterable<Ingredient>    findAll();
    Ingredient              findOne(String id);
    Ingredient              save(Ingredient ingredient);
}
