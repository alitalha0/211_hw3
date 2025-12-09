package penguingame.objects.penguins;

import java.util.ArrayList;
import java.util.List;

import penguingame.enums.Direction;
import penguingame.objects.ISlidable;
import penguingame.objects.TerrainObject;
import penguingame.objects.food.Food;
import penguingame.terrain.IcyTerrain;

public abstract class Penguin extends TerrainObject implements ISlidable {

    protected String name;
    protected List<Food> collectedFood;
    protected boolean specialActionUsed;
    protected boolean isStunned;
    protected boolean isRemoved;

    public Penguin(String name) {
        this.name = name;
        this.collectedFood = new ArrayList<>();
        this.specialActionUsed = false;
        this.isStunned = false;
        this.isRemoved = false;
        this.displaySymbol = name;

    }

    public String getName() { return name; }

    public List<Food> getCollectedFood() { return new ArrayList<>(collectedFood); }

    public boolean isSpecialActionUsed() { return specialActionUsed; }

    public boolean isStunned() { return isStunned; }

    public boolean isRemoved() { return isRemoved; }

    public void setStunned(boolean stunned) { isStunned = stunned; }

    public void setRemoved(boolean removed) { isRemoved = removed; }

    public void collectFood(Food food) {
        collectedFood.add(food);
    }

    public Food removeLightestFood() {
        if (collectedFood.isEmpty()) { 
            return null; 
        }  
        Food lightest = collectedFood.get(0);

        for (Food food : collectedFood) {
            if (food.getWeight() < lightest.getWeight()) {
                lightest = food;
            }
        }

        collectedFood.remove(lightest);
        return lightest;
    }

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
    
    @Override
    public boolean canSlide() { return true; }

    @Override
    public void slide(Direction direction, IcyTerrain terrain) {
        terrain.slide(this, direction);
    }

    public abstract void useSpecialAction(Direction dir, IcyTerrain terrain);
    public abstract String getPenguinTypeName();
}
