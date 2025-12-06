public class CollisionResult {
    public boolean stopSliding;
    public boolean transferMovement;
    public ITerrainObject newSlider;
    public Direction newDirection;
    
    public CollisionResult(boolean stopSliding, boolean transferMovement, ITerrainObject newSlider, Direction newDirection) {
        this.stopSliding = stopSliding;
        this.transferMovement = transferMovement;
        this.newSlider = newSlider;
        this.newDirection = newDirection;
    }
    
}