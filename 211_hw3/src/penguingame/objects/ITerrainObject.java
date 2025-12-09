package penguingame.objects;
import penguingame.util.Position;

public interface ITerrainObject {
    Position getPosition();
    void setPosition(Position position);
    String getDisplaySymbol();
    
}