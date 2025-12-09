package penguingame.objects;
import penguingame.util.Position;

// An interface for all terrain objects that is on the terrain -> all objects must have position and a diplay symbol.
public interface ITerrainObject {
    Position getPosition();
    void setPosition(Position position);
    String getDisplaySymbol();
    
}