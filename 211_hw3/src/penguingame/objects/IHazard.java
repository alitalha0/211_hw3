package penguingame.objects;
import penguingame.enums.Direction;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;

// Hazard interface -> all hazards must handle collision.
public interface IHazard extends ITerrainObject {
    CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir);
}