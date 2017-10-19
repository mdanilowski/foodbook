package pl.mdanilowski.foodbook.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredient implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("count")
    private int count;
    @SerializedName("weight")
    private int weight;
    @SerializedName("unit")
    private int unit;

    public Ingredient() {
    }

    public Ingredient(String name, int count, int weight, int unit) {
        this.name = name;
        this.count = count;
        this.weight = weight;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
