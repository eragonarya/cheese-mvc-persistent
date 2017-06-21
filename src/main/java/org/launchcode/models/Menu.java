package org.launchcode.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody on 6/20/2017.
 */
@Entity
public class Menu {
    @NotNull
    @Size(min=3,max=15)
    private String name;

    @Id
    @GeneratedValue
    private int id;

    @ManyToMany
    private List<Cheese> cheeses = new ArrayList<>();

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public List<Cheese> getCheeses() {
        return cheeses;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void addItem(Cheese item){
        cheeses.add(item);
    }

    public Menu(){};
    public Menu(String name){
        this.setName(name);
    }
}
