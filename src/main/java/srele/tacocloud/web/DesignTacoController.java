package srele.tacocloud.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import srele.tacocloud.Ingredient;
import srele.tacocloud.Ingredient.Type;
import srele.tacocloud.Taco;
import srele.tacocloud.data.IngredientRepository;
import srele.tacocloud.data.JdbcIngredientRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    private IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo){
        this.ingredientRepo=ingredientRepo;
    }

    @GetMapping
    public String showDesignFrom(Model model){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i->ingredients.add(i));

        Type[] types = Ingredient.Type.values();

        for (Type type : types){
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients,type));
        }
        model.addAttribute("design",new Taco());

        return "design";

    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors){
        if (errors.hasErrors()){
            return "design";
        }

        log.info("Processing design: " + design);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
        List<Ingredient> filteredList = new ArrayList<>();
        for(Ingredient ingredient  : ingredients) {
            if (ingredient.getType() == type){
                filteredList.add(ingredient);
            }
        }

        return filteredList;
    }
}
