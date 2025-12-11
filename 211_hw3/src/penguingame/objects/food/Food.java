package penguingame.objects.food;
import penguingame.enums.FoodType;
import penguingame.objects.TerrainObject;

// Food class for all types of food.
public class Food extends TerrainObject {
    private FoodType type;
    private int weight;

    public Food(FoodType type, int weight) {
        this.type = type;
        this.weight = weight;
        this.displaySymbol = type.getShortHand();
    }

    public FoodType getType() {
        return type;
    }

    @Override 
    public String toString() {
        return type.getShortHand() + " (" + weight + " units)";
    }

    public int getWeight() { return weight; }
    
}
