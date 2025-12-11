package penguingame.objects;

import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;

//slidable interface -> all slidable terrain objects must have function that makes them slide.
public interface ISlidable {
    boolean canSlide();
    void slide(Direction dir, IcyTerrain terrain);
}
