public class RoyalPenguin extends Penguin {
    public RoyalPenguin(String name) { super(name); }
    
    @Override
    public void useSpecialAction(Direction dir, IcyTerrain terrain) {
        terrain.moveOneSquare(this, dir);
        specialActionUsed = true;
    }
    
    @Override
    public String getPenguinTypeName() { return "Royal Penguin"; }
}
