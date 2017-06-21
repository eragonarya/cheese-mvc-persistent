package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Cody on 6/20/2017.
 */
@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    MenuDao menuDao;
    @Autowired
    CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model){
        model.addAttribute("title", "Menu");
        model.addAttribute("menus", menuDao.findAll());
        return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String Processadd(Model model, @ModelAttribute @Valid Menu menu, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add Menu");
            model.addAttribute(menu);
            return "menu/add";
        }
        else{
            menuDao.save(menu);
            return "redirect:view/" + menu.getId();
        }
    }

    @RequestMapping(value="view/{id}", method=RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id){
        Menu menu = menuDao.findOne(id);
        model.addAttribute("menu", menu);
        model.addAttribute("title", "Viewing " + menu.getName());
        return "menu/view";
    }

    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", form);
        model.addAttribute("title","Add item to menu: " + menu.getName());
        return "menu/add-item";
    }

    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String processAddItem(Model model,AddMenuItemForm form, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("form", form);
            model.addAttribute("title", "Add item to menu: " + menuDao.findOne(form.getMenuId()));
            return "menu/add-item";
        }
        else{
            Cheese cheese = cheeseDao.findOne(form.getCheeseId());
            Menu menu = menuDao.findOne(form.getMenuId());
            menu.addItem(cheese);
            menuDao.save(menu);
            return "redirect:/menu/view/" + menu.getId();
        }
    }
}
