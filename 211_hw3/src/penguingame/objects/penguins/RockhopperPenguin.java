package penguingame.objects.penguins;
import penguingame.enums.Direction;
import penguingame.terrain.IcyTerrain;

public class RockhopperPenguin extends Penguin {
    private boolean preparedToJump;
    
    public RockhopperPenguin(String name) {
        super(name);
        this.preparedToJump = false;
    }
    
    public boolean isPreparedToJump() { return preparedToJump; }
    public void setPreparedToJump(boolean prepared) { this.preparedToJump = prepared; }
    
    @Override
    public void useSpecialAction(Direction dir, IcyTerrain terrain) {
        this.preparedToJump = true;
        specialActionUsed = true;
    }
    
    @Override
    public String getPenguinTypeName() { return "Rockhopper Penguin"; }
}