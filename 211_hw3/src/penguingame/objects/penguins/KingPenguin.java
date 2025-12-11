package penguingame.objects.penguins;

import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;
//Penguin type that can choose to stop at the 5th square across the chosen direction.
public class KingPenguin extends Penguin {
    public KingPenguin(String name) { super(name); }
    
    @Override 
    public void useSpecialAction(Direction dir, IcyTerrain terrain) {
        terrain.slideWithStop(this, dir, 5); //slides but stops at 5th sqaure   
        specialActionUsed = true;
    }
    
    @Override
    public String getPenguinTypeName() { return "King Penguin"; }
}