package pl.mdanilowski.foodbook.model.userUpdates;


import pl.mdanilowski.foodbook.model.Recipe;

public class UserUpdatesBase {

    private long addDate;

    private Recipe recipe;

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
