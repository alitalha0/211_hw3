package penguingame.objects.penguins;
import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;
// Penguin that is able to move one square before sliding.
public class RoyalPenguin extends Penguin {
    public RoyalPenguin(String name) { super(name); }
    
    @Override
    public void useSpecialAction(Direction dir, IcyTerrain terrain) {
        terrain.moveOneSquare(this, dir); //it's speacial action is moving one square before sliding
        specialActionUsed = true; // can do this only once per turn. ( flag becomes false after sliding. )
    }
    
    @Override
    public String getPenguinTypeName() { return "Royal Penguin"; }
}
