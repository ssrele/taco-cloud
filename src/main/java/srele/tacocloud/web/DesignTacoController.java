package srele.tacocloud.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import srele.tacocloud.Ingredient;
import srele.tacocloud.Ingredient.Type;
import srele.tacocloud.Order;
import srele.tacocloud.Taco;
import srele.tacocloud.data.IngredientRepository;
import srele.tacocloud.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private IngredientRepository ingredientRepo;

    private TacoRepository designRepo;

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo,
                                TacoRepository designRepo){
        this.ingredientRepo=ingredientRepo;
        this.designRepo=designRepo;
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
    public String processDesign(@Valid Taco design,
                                Errors errors, @ModelAttribute Order order, RedirectAttributes attributes){
        if (errors.hasErrors()){
            log.error("Processing design  errors: " + errors);
            return "design";
        }

        log.info("Processing design: " + design);


        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        log.info("for order " + order);
        attributes.addFlashAttribute("order",order);
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
