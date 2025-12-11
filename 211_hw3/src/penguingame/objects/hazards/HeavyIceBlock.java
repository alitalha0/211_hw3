package penguingame.objects.hazards;

import penguingame.enums.Direction;
import penguingame.objects.IHazard;
import penguingame.objects.ITerrainObject;
import penguingame.objects.TerrainObject;
import penguingame.objects.penguins.Penguin;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;

public class HeavyIceBlock extends TerrainObject implements IHazard {
    public HeavyIceBlock() { this.displaySymbol = "HB"; } // sets the display symbol for displaying correctly
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        if (collider instanceof Penguin) { //removes penguin's lightest food collected if there is a collision, penguin is not affected if it havent collected any
            ((Penguin) collider).removeLightestFood();
        }
        return new CollisionResult(true, false, null, dir);
    }
}
