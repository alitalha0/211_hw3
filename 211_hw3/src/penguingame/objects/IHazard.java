package penguingame.objects;
import penguingame.enums.Direction;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;

public interface IHazard extends ITerrainObject {
    CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir);
}