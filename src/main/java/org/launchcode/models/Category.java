package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cody on 6/19/2017.
 */
@Entity
public class Category {
    @Id
    @GeneratedValue
    private int id;

    @NotNull(message="Name can not be empty")
    @Size(min=3, max=15, message="Name has an incorrect number of characters")
    private String name;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Cheese> cheeses = new ArrayList<>();

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public Category(){}
    public Category(String name){
        this.setName(name);
    }


}
