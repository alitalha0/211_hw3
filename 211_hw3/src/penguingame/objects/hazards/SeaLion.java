package penguingame.objects.hazards;

import penguingame.enums.Direction;
import penguingame.objects.IHazard;
import penguingame.objects.ISlidable;
import penguingame.objects.ITerrainObject;
import penguingame.objects.TerrainObject;
import penguingame.objects.penguins.Penguin;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;
// a SeaLion hazard object which slides the same direction as penguin after the collision and makes the penguin slide to the opposite direction.
public class SeaLion extends TerrainObject implements IHazard, ISlidable {
    public SeaLion() { this.displaySymbol = "SL"; }
    
    @Override
    public boolean canSlide() { return true; } // slidable
    
    @Override
    public void slide(Direction direction, IcyTerrain terrain) {
        terrain.slide(this, direction);
    }
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        // if collider is a slidable object
        if (collider instanceof Penguin || collider instanceof LightIceBlock) {
            terrain.slide(this, dir);
            if (collider instanceof Penguin) {
                return new CollisionResult(true, true, collider, dir.getOpposite());
            } else {
                return new CollisionResult(true, false, null, dir);
            }
        }
        return new CollisionResult(true, false, null, dir);
    }
}