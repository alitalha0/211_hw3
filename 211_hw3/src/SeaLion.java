public class SeaLion extends TerrainObject implements IHazard, ISlidable {
    public SeaLion() { this.displaySymbol = "SL"; }
    
    @Override
    public boolean canSlide() { return true; }
    
    @Override
    public void slide(Direction direction, IcyTerrain terrain) {
        terrain.slide(this, direction);
    }
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
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