package penguingame.terrain;

import java.util.ArrayList;
import java.util.List;
import penguingame.enums.Direction;
import penguingame.objects.IHazard;
import penguingame.objects.ISlidable;
import penguingame.objects.ITerrainObject;
import penguingame.objects.food.Food;
import penguingame.objects.hazards.HoleInIce;
import penguingame.objects.hazards.LightIceBlock;
import penguingame.objects.hazards.SeaLion;
import penguingame.objects.penguins.Penguin;
import penguingame.objects.penguins.RockhopperPenguin;
import penguingame.util.Position;

//IcyTerrain object that handles and displays the grid
public class IcyTerrain {
    private List<List<List<ITerrainObject>>> grid;
    // Each cell is a List<IterainObject> because multiple objects can temporarily occupy the same space.
    // those Lists of individual cells are contained in a List that represents the columns; List<List<IterrainObject>>
    // column list is contained in the List that represents row; -> List<List<List<ITerrainObject>>>
    // and row lists are contained in grid List<List<List<ITerrainObject>>> = grid;

    public IcyTerrain() {
        grid = new ArrayList<>(); // list of all squares on the 10*10 grid as mentioned above
        for(int i=0; i<10; i++) {
            List<List<ITerrainObject>> row = new ArrayList<>();
            
            for(int j=0;j<10;j++) {
                row.add(new ArrayList<>()); //Empty Cell
            }
            grid.add(row);
        }
    }

    // returns the list of object that are at the given position
    // Multiple objects can be at the same position before collision is handled, therefore a list is used
    public List<ITerrainObject> getObjectsAt(Position position) {
        if(!position.isValid()) {
            return new ArrayList<>();
        }
        return grid.get(position.getRow()).get(position.getCol());
    }

    // adding an object to a location
    public void addObject(ITerrainObject objectToAdd, Position position) {
        if (position.isValid()) {
            grid.get(position.getRow()).get(position.getCol()).add(objectToAdd);
            objectToAdd.setPosition(position);
        }
    }

    // removing the given object from the terrain
    public void removeObject(ITerrainObject objectToRemove) {
        if (objectToRemove == null) { return; }
        Position pos = objectToRemove.getPosition();
        if (pos.isValid()) {
            grid.get(pos.getRow()).get(pos.getCol()).remove(objectToRemove);  
        }
    }

    // checks if given square is empty
    public boolean isEmpty(Position pos) {
        return pos.isValid() && getObjectsAt(pos).isEmpty();
    }

    //sliding = moving one square at the time back to back
    public void moveOneSquare(Penguin penguin, Direction dir) {

        //determining the current and next position
        Position current = penguin.getPosition();
        Position next = current.getNextPosition(dir);
        
        removeObject(penguin);// removing the object to move it
        
        //if next tile is not valid, penguin is removed from the game
        if (!next.isValid()) {
            penguin.setRemoved(true);
            return;
        }
        

        
        List<ITerrainObject> objectsAtNext = getObjectsAt(next);
        for (ITerrainObject obj : objectsAtNext) {
            if (obj instanceof IHazard) {
                // Trigger the collision logic (falling in hole, bouncing off sea lion, etc.)
                ((IHazard) obj).handleCollision(penguin, this, dir);
                
        // If the penguin wasn't removed (e.g., just bounced), ensure it's on the board
        //  handleCollision often handles placement/sliding, so we only add if the penguin is still active and hasn't been moved by the collision handler.
                if (!penguin.isRemoved() && penguin.getPosition() == null) {
                    addObject(penguin, next);
                }
                return;
            }
        }

        addObject(penguin, next);
        handlePositionInteractions(next);
    }

    // method that handles the special action King and Emperor penguins.
    public void slideWithStop(Penguin penguin, Direction dir, int stopAtSquare) {

        Position current = penguin.getPosition();
        removeObject(penguin);
        int squaresMoved = 0;

        while (true) { 
            Position next = current.getNextPosition(dir);

            if(!(next.isValid())) {
                penguin.setRemoved(true);
                return;
            }

            List<ITerrainObject> objectsAtNext = getObjectsAt(next);

            // Checks if its a Plugged Hole (Safe to pass)
            boolean isPluggedHole = false;
            if (!objectsAtNext.isEmpty()) {
                ITerrainObject obj = objectsAtNext.get(0);
                if (obj instanceof HoleInIce && ((HoleInIce) obj).isPlugged()) {
                    isPluggedHole = true;
                }
            }

            // Allow move if Empty or Plugged Hole
            if (objectsAtNext.isEmpty() || isPluggedHole) {
                current = next;
                squaresMoved++;

                if (squaresMoved == stopAtSquare) {
                    addObject(penguin, current);
                    handlePositionInteractions(current);
                    return;
                }
            } else {
                addObject(penguin,current);
                handleCollision(penguin, objectsAtNext.get(0), dir, current); {
                    return;
                }

            }

        }

    }

    public void slide(ITerrainObject slider, Direction dir) {
        Position current = slider.getPosition();
        removeObject(slider);

        boolean canJump = false;
            if (slider instanceof RockhopperPenguin) {
            RockhopperPenguin rp = (RockhopperPenguin) slider;
            canJump = rp.isPreparedToJump();

        }

        while (true) { 
            Position next = current.getNextPosition(dir);
            
            if (!(next.isValid())) {
                if (slider instanceof Penguin) {
                    ((Penguin) slider).setRemoved(true);
                }
                return;
            }

            List<ITerrainObject> objectsAtNext = getObjectsAt(next);

            // 1. Check if the square is empty OR if it is a Plugged Hole (Safe to pass)
            boolean isPluggedHole = false;
            if (!objectsAtNext.isEmpty()) {
                ITerrainObject obj = objectsAtNext.get(0);
                if (obj instanceof HoleInIce && ((HoleInIce) obj).isPlugged()) {
                    isPluggedHole = true;
                }
            }
            // if next tile is empty or it is PH, then it is safe to move directly by settin current position to next
            if (objectsAtNext.isEmpty() || isPluggedHole) {
                //that means it is safe to slide, update the position and continue the loop
                current = next;
                // If it's a plugged hole, we technically "occupy" it briefly while passing through (priority)
                continue;

            } else { // if next tile is not empty: 
                ITerrainObject blocker = objectsAtNext.get(0);

                // logic to allow jumping if target has Food (but not hazards/penguins)
                if (canJump && blocker instanceof IHazard) {
                    Position jumpTo = next.getNextPosition(dir);
                    
                    // Check if jumpTo is safe (is Valid and no Hazard AND no Penguin)
                    // We manually check objects to allow Food
                    boolean isSafeJump = jumpTo.isValid();
                    if (isSafeJump) {
                        for (ITerrainObject obj : getObjectsAt(jumpTo)) {
                            if (obj instanceof IHazard || obj instanceof Penguin) {
                                isSafeJump = false;
                                break;
                            }
                        }
                    }

                    if(isSafeJump) {
                        current = jumpTo;
                        canJump = false;
                        ((RockhopperPenguin)slider).setPreparedToJump(false);
                        
                        // Important: If we jumped onto Food, we must handle it (collect & stop)
                        // If handlePositionInteractions collects food, we should probably stop sliding
                        // to adhere to "stop on food" rule.
                        addObject(slider, current); // Temporarily place to check interactions
                        handlePositionInteractions(current);
                        
                        // If food was collected (grid is now empty or contains us), we stop.
                        // If empty (no food), we continue sliding.
                        if (!isEmpty(current) && !(getObjectsAt(current).get(0) instanceof Penguin)) {
                             // Food likely still there or other interaction
                        } else if (getObjectsAt(current).size() == 1 && getObjectsAt(current).get(0) == slider) {
                             // We are the only thing there (food collected), stop!
                             return; 
                        }
                        
                        // If it was truly empty, continue sliding
                        removeObject(slider); 
                        continue;
                    }
                    else {
                        canJump = false;
                        ((RockhopperPenguin)slider).setPreparedToJump(false);
                    }
                }
                if (slider instanceof Penguin && blocker instanceof Food) {
                    // Penguin stops ON the food square, not before it
                    addObject(slider, next);
                    handlePositionInteractions(next);
                    return;
                }
                addObject(slider, current);
                handleCollision(slider, blocker, dir, current);
                return;
            }
            
        }
    }

    private void handleCollision(ITerrainObject slider, ITerrainObject blocker, Direction dir, Position sliderPos) {
        //  Penguin moves to the food square, then collects it.
        if (slider instanceof Penguin && blocker instanceof Food) {
            Penguin p = (Penguin) slider;
            Food f = (Food) blocker;
            
            // visually move penguin to the food's position
            removeObject(p);
            addObject(p, blocker.getPosition());
            
            // collect food and remove it from board
            p.collectFood(f);
            removeObject(f);
            
            // return immediately (stops sliding because we returned from handleCollision)
            return;
        }

        if (slider instanceof Penguin && blocker instanceof Penguin) {
            slide(blocker, dir);
            return;
        }
        
        // handles the collision in the case of collision with the food. (Non-penguin with food)
        if (slider instanceof ISlidable && blocker instanceof Food) {
            removeObject(blocker);
            removeObject(slider);
            slide(slider, dir);
            return;
        }

        if ((slider instanceof LightIceBlock || slider instanceof SeaLion) && blocker instanceof Penguin ) {
            return; 
        }

        if (blocker instanceof IHazard) {
            CollisionResult result = ((IHazard) blocker).handleCollision(slider, this, dir);
            if (result.transferMovement && result.newSlider != null) {
                removeObject(result.newSlider);
                slide(result.newSlider, result.newDirection);
            }
        }
    }

    // handles the interaction between food and penguin in a tile.
    private void handlePositionInteractions(Position pos) {
        List<ITerrainObject> objects = getObjectsAt(pos); // objects at the given position
        
        Penguin penguin = null;
        List<Food> foodItems = new ArrayList<>();
        
        for (ITerrainObject obj : objects) {
            if (obj instanceof Penguin) {
                penguin = (Penguin) obj;
            } else if (obj instanceof Food) {
                foodItems.add((Food) obj);
            }
        }
        //penguin collects the food in the square. Food is then removed
        if (penguin != null) {
            for (Food food : foodItems) {
                penguin.collectFood(food);
                removeObject(food);
            }
        }
    }

    //displays the grid in terminal for the user
    public void displayGrid() {
        System.out.println("---------------------------------------------------");

        // displaying rows
        for (int row = 0; row < 10; row++) {
            System.out.print("|");
            //displaying each individual square within the row
            for (int col = 0; col < 10; col++) {
                Position pos = new Position(row, col);
                List<ITerrainObject> objects = getObjectsAt(pos);
                
                if (objects.isEmpty()) {
                    System.out.print("    |");
                } else {
                    ITerrainObject toDisplay = getPriorityObject(objects); // only the most prioritized object in the position.
                    String symbol = toDisplay.getDisplaySymbol();
                    int padding = (4 - symbol.length()) / 2;
                    String paddedSymbol = " ".repeat(padding) + symbol + 
                                         " ".repeat(4 - symbol.length() - padding); // adjusts the string to fit in the square
                    System.out.print(paddedSymbol + "|");
                }
            }
            System.out.println();
            System.out.println("---------------------------------------------------"); 
        }
    }

    // getting the priority object in the position to display.
    private ITerrainObject getPriorityObject(List<ITerrainObject> objects) {
        for (ITerrainObject obj : objects) {
            if (obj instanceof Penguin) { 
                return obj; 
            }
        }
        for (ITerrainObject obj : objects) {
            if (obj instanceof IHazard) { 
                return obj;
            }    
        }
        return objects.get(0);
    }   

}
