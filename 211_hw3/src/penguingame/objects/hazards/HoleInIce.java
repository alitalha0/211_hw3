package penguingame.objects.hazards;

import penguingame.enums.Direction;
import penguingame.objects.IHazard;
import penguingame.objects.ITerrainObject;
import penguingame.objects.TerrainObject;
import penguingame.objects.penguins.Penguin;
import penguingame.terrain.CollisionResult;
import penguingame.terrain.IcyTerrain;
// HoleInIce class. ( a water filled hole that penguins can block if they ever fall into it.)
// Slidable objects can go over it if it is plugged
public class HoleInIce extends TerrainObject implements IHazard {
    private boolean isPlugged;
    
    public HoleInIce() {
        this.displaySymbol = "HI";
        this.isPlugged = false;
    }
    
    public boolean isPlugged() { return isPlugged; }
    
    public void plug() {
        this.isPlugged = true;
        this.displaySymbol = "PH"; // changes the display symbol to PH indicating that it is plugged by a penguin.
    }
    
   @Override
    public CollisionResult handleCollision(ITerrainObject collider, IcyTerrain terrain, Direction dir) {
        if (isPlugged) {
            return new CollisionResult(false, false, null, dir);
        }
        
        if (collider instanceof Penguin) {
            // Print the specific message required by the assignment
            System.out.println(collider.getDisplaySymbol() + " falls into the water due to HI in its path."); //informing the user about the plugged hole.
            
            // Remove from terrain FIRST (while it's still at the previous square)
            ((Penguin) collider).setRemoved(true);
            terrain.removeObject(collider); 

            // THEN update position (visually purely for logic if needed, though it's gone)
            collider.setPosition(this.getPosition());
        } else if (collider instanceof LightIceBlock || collider instanceof SeaLion) {
            terrain.removeObject(collider);
            plug();
        }
        return new CollisionResult(true, false, null, dir); // returns a collision result object accordingly
    }
}