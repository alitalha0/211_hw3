package penguingame.objects.penguins;

import java.util.ArrayList;
import java.util.List;
import penguingame.enums.Direction;
import penguingame.objects.ISlidable;
import penguingame.objects.TerrainObject;
import penguingame.objects.food.Food;
import penguingame.terrain.IcyTerrain;

// Abstract Penguin class for all Penguin Types to inherit 
public abstract class Penguin extends TerrainObject implements ISlidable {

    protected String name;
    protected List<Food> collectedFood; //list of collected foods
    protected boolean specialActionUsed; // flag that indicates whether specail action is used
    protected boolean isStunned; // flag that indicates whether penguin is stunned.
    protected boolean isRemoved; // turn is skipped if ai penguin is removed

    public Penguin(String name) {
        this.name = name;
        this.collectedFood = new ArrayList<>();
        this.specialActionUsed = false;
        this.isStunned = false;
        this.isRemoved = false;
        this.displaySymbol = name;

    }

    // some getters and setters
    public String getName() { return name; }

    public List<Food> getCollectedFood() { return new ArrayList<>(collectedFood); }

    public boolean isSpecialActionUsed() { return specialActionUsed; }

    public boolean isStunned() { return isStunned; }

    public boolean isRemoved() { return isRemoved; }

    public void setStunned(boolean stunned) { isStunned = stunned; }

    public void setRemoved(boolean removed) { isRemoved = removed; }

    // collecting food if it is available (food: food that is at the same square as the penguin)
    public void collectFood(Food food) {
        collectedFood.add(food);
    }

    // removes the lightest food that penguin carries if penguin crashes with a HeavyIceBlock object
    public Food removeLightestFood() {
        if (collectedFood.isEmpty()) { 
            return null; 
        }  
        Food lightest = collectedFood.get(0);

        for (Food food : collectedFood) {// loop that determines the lightest food.
            if (food.getWeight() < lightest.getWeight()) {
                lightest = food;
            }
        }

        collectedFood.remove(lightest);
        return lightest;
    }

    // determines the total food weight. Useful for determining the rank at the end of the game.
    public int getTotalFoodWeight() {
        if (collectedFood.isEmpty() || collectedFood == null) {
            return 0;
        }
        int total = 0;
        for (Food food : collectedFood) {
            total += food.getWeight();
        }
        return total;
    }
    
    @Override // determines whether penguin can slide or not.
    public boolean canSlide() { return true; }


    @Override //sliding
    public void slide(Direction direction, IcyTerrain terrain) {
        terrain.slide(this, direction);
    }

    public abstract void useSpecialAction(Direction dir, IcyTerrain terrain); // abstract function since all penguin's specail action are different
    public abstract String getPenguinTypeName(); // abstract because P1,P2 and P3 may have different types
}
