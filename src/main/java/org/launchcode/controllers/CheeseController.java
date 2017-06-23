package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");
        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors,@RequestParam int categoryId, Model model) {
        Category cat = categoryDao.findOne(categoryId);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute(newCheese);
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }
    @RequestMapping(value="/edit/{cheeseId}", method=RequestMethod.GET)
    public String editCheese(@PathVariable int cheeseId, Model model){
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute(cheeseDao.findOne(cheeseId));
        model.addAttribute("cheeseID", cheeseId);
        model.addAttribute("title", cheeseDao.findOne(cheeseId).getName());
        return "cheese/edit";
    }
    @RequestMapping(value="/edit", method = RequestMethod.POST)
    public String processEditCheese(Model model,@RequestParam int cheeseId, @ModelAttribute @Valid Cheese cheese, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute(cheese);
            model.addAttribute("cheeseID", cheeseId);
            model.addAttribute("title", cheeseDao.findOne(cheeseId).getName());
            return"cheese/edit";
        }
        else {
            Cheese editCheese = cheeseDao.findOne(cheeseId);
            editCheese.setName(cheese.getName());
            editCheese.setCategory(editCheese.getCategory());
            editCheese.setDescription(editCheese.getDescription());
            cheeseDao.save(editCheese);
            return "redirect:";
        }
    }

    @RequestMapping(value="/category/{id}")
    public String category(Model model, @PathVariable int id){
        List<Cheese> cheeses = new ArrayList<>();
        for(Cheese c: cheeseDao.findAll()){
            if(c.getCategory().getId()== id){
                cheeses.add(c);
            }
        }
        model.addAttribute("title", categoryDao.findOne(id));
        model.addAttribute("cheeses", cheeses);
        return "cheese/index";
    }

}
