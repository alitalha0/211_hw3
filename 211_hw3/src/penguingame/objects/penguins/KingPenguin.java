package penguingame.objects.penguins;

import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;

public class KingPenguin extends Penguin {
    public KingPenguin(String name) { super(name); }
    
    @Override
    public void useSpecialAction(Direction dir, IcyTerrain terrain) {
        terrain.slideWithStop(this, dir, 5);
        specialActionUsed = true;
    }
    
    @Override
    public String getPenguinTypeName() { return "King Penguin"; }
}