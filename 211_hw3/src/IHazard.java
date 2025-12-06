public interface IHazard extends ITerrainObject {
    CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir);
}