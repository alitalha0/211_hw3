public class HeavyIceBlock extends TerrainObject implements IHazard {
    public HeavyIceBlock() { this.displaySymbol = "HB"; }
    
    @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        if (collider instanceof Penguin) {
            ((Penguin) collider).removeLightestFood();
        }
        return new CollisionResult(true, false, null, dir);
    }
}
