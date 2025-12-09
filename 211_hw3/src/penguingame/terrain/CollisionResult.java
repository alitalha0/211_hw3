package penguingame.terrain;
import penguingame.enums.Direction;
import penguingame.objects.ITerrainObject;

// Object that represents object's state after a collision.
public class CollisionResult {
    public boolean stopSliding; // if object's sliding is interfered
    public boolean transferMovement; // transfering movement ( seaLion and LightIceBlock )
    public ITerrainObject newSlider;
    public Direction newDirection;
    
    public CollisionResult(boolean stopSliding, boolean transferMovement, ITerrainObject newSlider, Direction newDirection) {
        this.stopSliding = stopSliding;
        this.transferMovement = transferMovement;
        this.newSlider = newSlider;
        this.newDirection = newDirection;
    }
    
}