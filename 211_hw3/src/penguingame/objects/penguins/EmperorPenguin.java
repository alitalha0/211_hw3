package penguingame.objects.penguins;

import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;
// Penguin type that can chooes to stop at the 3rd square across chosen direction.
public class EmperorPenguin extends Penguin {
    public EmperorPenguin(String name) { super(name); }
    
    @Override
    public void useSpecialAction(Direction dir, IcyTerrain terrain) {
        terrain.slideWithStop(this, dir, 3);
        specialActionUsed = true;
    }
    
    @Override
    public String getPenguinTypeName() { return "Emperor Penguin"; }
}