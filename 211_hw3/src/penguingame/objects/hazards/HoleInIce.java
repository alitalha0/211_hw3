package penguingame.objects.hazards;

import penguingame.enums.Direction;
import penguingame.objects.IHazard;
import penguingame.objects.ITerrainObject;
import penguingame.objects.TerrainObject;
import penguingame.objects.penguins.Penguin;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;

public class HoleInIce extends TerrainObject implements IHazard {
    private boolean isPlugged;
    
    public HoleInIce() {
        this.displaySymbol = "HI";
        this.isPlugged = false;
    }
    
    public boolean isPlugged() { return isPlugged; }
    
    public void plug() {
        this.isPlugged = true;
        this.displaySymbol = "PH";
    }
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        if (isPlugged) {
            return new CollisionResult(false, false, null, dir);
        }
        
        if (collider instanceof Penguin) {
            ((Penguin) collider).setRemoved(true);
            terrain.removeObject(collider);
        } else if (collider instanceof LightIceBlock || collider instanceof SeaLion) {
            terrain.removeObject(collider);
            plug();
        }
        return new CollisionResult(true, false, null, dir);
    }
}