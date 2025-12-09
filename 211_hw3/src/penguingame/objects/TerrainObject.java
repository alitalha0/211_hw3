package penguingame.objects;
import penguingame.util.Position;

/**
 * Abstract base class for all terrain objects
 */
public abstract class TerrainObject implements ITerrainObject {
    protected Position position;
    protected String displaySymbol;
    
    /**
     * Default constructor.
     * Position will be set when object is added to terrain.
     * DisplaySymbol should be set by subclass constructors.
     */
    public TerrainObject() {
        this.position = null;
        this.displaySymbol = null;
    }
    
    @Override
    public Position getPosition() { 
        return position; 
    }
    
    @Override
    public void setPosition(Position position) { 
        this.position = position; 
    }
    
    @Override
    public String getDisplaySymbol() { 
        return displaySymbol; 
    }
}