package penguingame.objects;

import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;

public interface ISlidable {
    boolean canSlide();
    void slide(Direction dir, IcyTerrain terrain);
}
