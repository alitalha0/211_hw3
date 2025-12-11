package penguingame.objects.hazards;

import penguingame.enums.Direction;
import penguingame.objects.IHazard;
import penguingame.objects.ISlidable;
import penguingame.objects.ITerrainObject;
import penguingame.objects.TerrainObject;
import penguingame.objects.penguins.Penguin;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;
// LightIceBlock object that can slide if another slidable object collides with it
public class LightIceBlock extends TerrainObject implements IHazard, ISlidable {
    
    
    public LightIceBlock() { 
        this.displaySymbol = "LB"; 
    }

    @Override
    public boolean canSlide() { return true; }
    
    @Override // sliding after the collision.
    public void slide(Direction direction, IcyTerrain terrain) {
        terrain.slide(this, direction);
    }
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        if (collider instanceof Penguin) {
            ((Penguin) collider).setStunned(true); // penguin is stunned after the collision. A turn is skipped.
        }
        terrain.slide(this, dir); // LightIceBlock object slides to the same direction with the penguin
        return new CollisionResult(true, false, null, dir); // returns a CollisionResult object accordingly
    }

    
}