public class LightIceBlock extends TerrainObject implements IHazard, ISlidable {
    
    
    public LightIceBlock() { 
        this.displaySymbol = "LB"; 
    }

    @Override
    public boolean canSlide() { return true; }
    
    @Override
    public void slide(Direction direction, IcyTerrain terrain) {
        terrain.slide(this, direction);
    }
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        if (collider instanceof Penguin) {
            ((Penguin) collider).setStunned(true);
        }
        terrain.slide(this, dir);
        return new CollisionResult(true, false, null, dir);
    }

    
}