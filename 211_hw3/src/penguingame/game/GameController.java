package penguingame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import penguingame.enums.Direction;
import penguingame.enums.FoodType;
import penguingame.enums.PenguinType;
import penguingame.objects.IHazard;
import penguingame.objects.ITerrainObject;
import penguingame.objects.food.Food;
import penguingame.objects.hazards.HeavyIceBlock;
import penguingame.objects.hazards.HoleInIce;
import penguingame.objects.hazards.LightIceBlock;
import penguingame.objects.hazards.SeaLion;
import penguingame.objects.penguins.EmperorPenguin;
import penguingame.objects.penguins.KingPenguin;
import penguingame.objects.penguins.Penguin;
import penguingame.objects.penguins.RockhopperPenguin;
import penguingame.objects.penguins.RoyalPenguin;
import penguingame.terrain.IcyTerrain;
import penguingame.util.Position;

/**
 * Main game controller that manages the entire Sliding Penguins Puzzle Game.
 * 
*/
public class GameController {
    // Core game components that stores the most important things
    private IcyTerrain terrain;           // The 10x10 icy grid
    private List<Penguin> penguins;       // All 3 penguins in the game (p1, p2, and p3)
    private Penguin playerPenguin;        // The penguin that assigned to the player
    
    // Utility objects
    private Scanner scanner;              // For reading user input
    private Random random;                // For random generation
    
    /**
     * Constructor initializes the scanner and random generator
     */
    public GameController() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }
    
    /**
     * Main method to start and run the entire game.
     * Handles the complete game flow 
     */
    public void startGame() {
        // Display welcome message
        System.out.println("Welcome to Sliding Penguins Puzzle Game App. An 10x10 icy terrain grid is being generated.");
        System.out.println("Penguins, Hazards, and Food items are also being generated. The initial icy terrain grid:");
        
        // Set up the game board and objects
        initializeGame();
        
        // Show the initial state of the grid
        terrain.displayGrid();
        
        // Display information about the penguins and which one is the player's
        displayPenguinInfo();
        
        // Main game loop: 4 turns for each of the 3 penguins
        for (int turn = 1; turn <= 4; turn++) {
            for (Penguin penguin : penguins) {
                playTurn(penguin, turn);
            }
        }
        
        // Game is over, show the final scoreboard
        displayGameOver();
    }
    
    /**
     * Initialize all game components:
     */
    private void initializeGame() {
        terrain = new IcyTerrain();
        
        // Generate 3 penguins and place them on edge squares. ( Edge positions are determined by a seperate private method)
        //It is important to generate penguins first to guarentee the available edge squares
        penguins = generatePenguins();
        
        // Randomly assign one penguin to the player
        playerPenguin = penguins.get(random.nextInt(3));
        
        // Place hazards on the grid
        generateHazards();
        
        // Place food items on the grid
        generateFood();
    }
    
    /**
     * Generates 3 penguins with random types and place them on edge squares.
     * returns the List of 3 generated penguins
     */
    private List<Penguin> generatePenguins() {

        List<Penguin> penguins = new ArrayList<>(); // an array list to store penguins
        PenguinType[] types = PenguinType.values(); // Array of penguin types
        
        // Create 3 penguins: P1, P2, P3
        for (int i = 0; i < 3; i++) {
            // Randomly select a penguin type (repetitions allowed)
            PenguinType type = types[random.nextInt(types.length)];
            String name = "P" + (i + 1);
            
            // Create the appropriate penguin subclass based on type
            Penguin penguin;
            switch(type) {
                case KING: 
                    penguin = new KingPenguin(name); 
                    break;
                case EMPEROR: 
                    penguin = new EmperorPenguin(name); 
                    break;
                case ROYAL: 
                    penguin = new RoyalPenguin(name); 
                    break;
                case ROCKHOPPER: 
                    penguin = new RockhopperPenguin(name); 
                    break;
                default: 
                    penguin = new KingPenguin(name);
            }
            
            // Find an available edge position for this penguin
            Position pos = getRandomEdgePosition();
            while (!terrain.isEmpty(pos)) {
                pos = getRandomEdgePosition();
            }
            
            // Place the penguin on the terrain
            terrain.addObject(penguin, pos);
            penguins.add(penguin);
        }
        
        return penguins;
    }
    
    /**
     * Generate 15 hazards with random types and place them on the grid.
     * Each hazard has an equal chance of being any of the 4 types.
     * Hazards cannot be placed where penguins already exist.
     */
    private void generateHazards() {
        // Create exactly 15 hazards
        for (int i = 0; i < 15; i++) {
            IHazard hazard;
            
            // Randomly select a hazard type (equal probability)
            int type = random.nextInt(4);
            
            switch(type) {
                case 0: 
                    hazard = new LightIceBlock(); 
                    break;
                case 1: 
                    hazard = new HeavyIceBlock(); 
                    break;
                case 2: 
                    hazard = new SeaLion(); 
                    break;
                case 3: 
                    hazard = new HoleInIce(); 
                    break;
                default: 
                    hazard = new LightIceBlock();
            }
            
            // Find an empty position (no penguins or other hazards)
            Position pos = getRandomPosition();
            while (!terrain.isEmpty(pos)) {
                pos = getRandomPosition();
            }
            
            // Place the hazard on the terrain
            terrain.addObject((ITerrainObject)hazard, pos);
        }
    }
    
    /**
     * Generate 20 food items with random types and weights.
     * Food items cannot be placed where penguins or hazards exist.
     */
    private void generateFood() {
        FoodType[] foodTypes = FoodType.values();
        
        // Create exactly 20 food items
        for (int i = 0; i < 20; i++) {
            // Randomly select food type and weight
            FoodType type = foodTypes[random.nextInt(foodTypes.length)]; // equal probability
            int weight = random.nextInt(5) + 1;  // Weight between 1-5
            Food food = new Food(type, weight);
            
            // Find a position that doesn't have penguins or hazards
            Position pos = getRandomPosition();
            while (!isPositionAvailableForFood(pos)) {
                pos = getRandomPosition();
            }
            
            // Place the food on the terrain
            terrain.addObject(food, pos);
        }
    }
    
    /**
     * Get a random position on the edge of the grid.
     */
    private Position getRandomEdgePosition() {
        int side = random.nextInt(4);
        // Edge squares are those with row=0, row=9, col=0, or col=9
        switch(side) {
            case 0: return new Position(0, random.nextInt(10));          // Top edge
            case 1: return new Position(9, random.nextInt(10));          // Bottom edge
            case 2: return new Position(random.nextInt(10), 0);          // Left edge
            case 3: return new Position(random.nextInt(10), 9);          // Right edge
            default: return new Position(0, 0);
        }
    }
    
    //Get a random position anywhere on the 10x10 grid.
    private Position getRandomPosition() {
        return new Position(random.nextInt(10), random.nextInt(10));
    }
    
    /**
     * Check if a position is available for placing food.
     * Food can be placed only if there are no penguins or hazards at that position.
     * pos: Position to check
     */
    private boolean isPositionAvailableForFood(Position pos) {
        List<ITerrainObject> objects = terrain.getObjectsAt(pos);
        
        // Check if any penguin or hazard is already at this position
        for (ITerrainObject obj : objects) {
            if (obj instanceof Penguin || obj instanceof IHazard) {
                return false;  // Position is occupied by penguin or hazard
            }
        }
        
        return true;  // Position is safe for food
    }
    
    /**
     * Display information about all penguins in the game.
     * Shows each penguin's name, type, and marks the player's penguin.
     */
    private void displayPenguinInfo() {
        System.out.println("\nThese are the penguins on the icy terrain:");
        
        for (Penguin penguin : penguins) {
            // Mark the player's penguin with an arrow
            // if penguin is playerPenguin it has a marker of ---> YOUR PENGUİN
            String marker = (penguin == playerPenguin) ? " ---> YOUR PENGUIN" : "";
            
            System.out.println("- Penguin " + penguin.getName().charAt(1) + 
                             " (" + penguin.getName() + "): " + 
                             penguin.getPenguinTypeName() + marker);
        }
    }
    
    /**
     * Execute a single turn for a penguin.
     * Handles both player turns and AI turns.
     * 
     *  penguin The penguin whose turn it is
     *  turnNumber The current turn number (1-4)
     */
    private void playTurn(Penguin penguin, int turnNumber) {
        // Skip turn if penguin has been removed from the game
        if (penguin.isRemoved()) {
            return;
        }
        
        // Displays turn header (states which's player turn it is)
        String turnLabel = (penguin == playerPenguin) ? 
            "*** Turn " + turnNumber + " " + penguin.getName() + " (Your Penguin):" :
            "*** Turn " + turnNumber + " " + penguin.getName() + ":";
        System.out.println("\n" + turnLabel);
        
        // Check if penguin is stunned (skips turn)
        if (penguin.isStunned()) {
            System.out.println(penguin.getName() + " is stunned and skips this turn.");
            penguin.setStunned(false);  // Remove stun for next turn to not penguin for keep Stunned.
            return;
        }
        
        // Execute turn based on whether it's player or AI
        if (penguin == playerPenguin) {
            playPlayerTurn(penguin);
        } else {
            playAITurn(penguin);
        }
        
        // Announce if penguin was removed during this turn
        if (penguin.isRemoved()) {
            System.out.println("*** " + penguin.getName() + " IS REMOVED FROM THE GAME!");
        }
        
        // Display the updated grid after the turn
        System.out.println("\nNew state of the grid:");
        terrain.displayGrid();
    }
    
    /**
     * Handle a player's turn by asking for input.
     * Player decides whether to use special action and which direction to move.
     * penguin: The player's penguin
     */
    private void playPlayerTurn(Penguin penguin) {

        //flag to determine whether player used his/her special action
        boolean useSpecial = false;
        
        // Ask if player wants to use special action (if not already used)
        if (!penguin.isSpecialActionUsed()) {
            useSpecial = getUserYesNo("Will " + penguin.getName() + 
                                     " use its special action? Answer with Y or N --> ");
        }
        
        // Asks for movement direction
        Direction dir = getUserDirection("Which direction will " + penguin.getName() + 
                                        " move? Answer with U (Up), D (Down), L (Left), R (Right) --> ");
        
        // Uses special action if player choses so
        if (useSpecial) {
            announceSpecialAction(penguin, dir, true);
            penguin.useSpecialAction(dir, terrain);
            
            // Royal Penguin's special action is just a single step
            if (penguin instanceof RoyalPenguin) {
                handlePositionAfterMove(penguin);
                return;  // Don't slide after using Royal penguins ability
            }
        }
        
        // Executes the normal slide movement
        penguin.slide(dir, terrain);
        
        // Announces any food collected or collided hazards
        handlePositionAfterMove(penguin);
    }
    
    /**
     * Handle an AI penguin's turn with automated decision making.
     * 
     * AI behavior:
     *  -special action handling
     * - Prioritizes moving toward food
     * - Otherwise moves toward hazards (except HoleInIce)
     * - Last resort: moves in any direction (even falling into water)
     * 
     *  penguin: The AI-controlled penguin
     */
    private void playAITurn(Penguin penguin) {
        boolean useSpecial = false;
        
        //chooses the best direction (Prioritizing Food)
        Direction chosenDir = chooseAIDirection(penguin); 
        
        // then checks if we need to use Special Action based on that choice
        if (penguin instanceof RockhopperPenguin && !penguin.isSpecialActionUsed()) {
            // Only checks for hazards on the specific  path we just chose
            List<Direction> hazardDirs = getDirectionsToHazards(penguin, false);
            
            // If the path we want to take has a hazard, we MUST jump
            if (hazardDirs.contains(chosenDir)) {
                useSpecial = true;
                System.out.println(penguin.getName() + " will automatically USE its special action.");
            }
        } 
        // Logic for other penguins (30% chance)
        else if (!penguin.isSpecialActionUsed() && random.nextDouble() < 0.3) {
            useSpecial = true;                      
            
            if (penguin instanceof RoyalPenguin) {
                System.out.println(penguin.getName() + " chooses to USE its special action.");
            } else {
                System.out.println(penguin.getName() + " chooses to use its special action.");
            }
        } else {
            System.out.println(penguin.getName() + " does NOT to use its special action.");
        }
        
        // Announcement
        System.out.println(penguin.getName() + " chooses to move " + getDirectionName(chosenDir) + ".");
        
        // Execution
        if (useSpecial) {
            announceSpecialAction(penguin, chosenDir, false);
            penguin.useSpecialAction(chosenDir, terrain);
            
            if (penguin instanceof RoyalPenguin) {
                handlePositionAfterMove(penguin);
                return;
            }
        }
        
        penguin.slide(chosenDir, terrain);
        handlePositionAfterMove(penguin);
    }
    
    /**
     * Announce what happens when a penguin uses its special action.
     * Different messages for different penguin types.
     * 
     * penguin: The penguin which using its special action
     * dir: The direction of the movement
     * isPlayer Whether this is the player's penguin
     */
    private void announceSpecialAction(Penguin penguin, Direction dir, boolean isPlayer) {
        if (penguin instanceof KingPenguin) {
            // King can stop at the 5th square
            // Announcement happens after the slide completes
        } else if (penguin instanceof EmperorPenguin) {
            // Emperor can stop at 3rd square
            // Announcement happens after the slide completes
        } else if (penguin instanceof RoyalPenguin) {
            // Royal moves one square
            if (!isPlayer) {
                // For AI, tries to find a safe direction
                Direction safeDir = getSafeDirectionForRoyal(penguin);
                if (safeDir != null) {
                    dir = safeDir;
                }
            }
            System.out.println(penguin.getName() + " moves one square to the " + 
                             getDirectionName(dir) + ".");
        } else if (penguin instanceof RockhopperPenguin) {
            // Rockhopper prepares to jump
            System.out.println(penguin.getName() + " prepares to jump over a hazard.");
        }
    }
    
    /**
     * Handle announcements after a penguin moves.
     * Announces food collection, falling into water, or hitting hazards.
     * 
     * @param penguin The penguin that just moved
     */
    private void handlePositionAfterMove(Penguin penguin) {
        // Check if whether penguin fell off the grid or not
        if (penguin.isRemoved()) {
            return;  // Will be announced in playTurn()
        }
        
        // Check if penguin collected any food
        List<Food> collectedFood = penguin.getCollectedFood();
        if (!collectedFood.isEmpty()) { // if penguin did not collect eny food
            Food lastFood = collectedFood.get(collectedFood.size() - 1);
            
            
            String foodTypeStr = lastFood.getType().toString();
            String foodName = foodTypeStr.charAt(0) + foodTypeStr.substring(1).toLowerCase();
            
            System.out.println(penguin.getName() + " takes the " + 
                             foodName + " on the ground. (Weight " + 
                             lastFood.getWeight() + " units)");
        }
    }
    
    /**
     * Chooses the best direction for an AI penguin to move.
     * 
     * Priorities
     * 1. Directions that lead to food (highest priority)
     * 2. Directions that lead to hazards (except HoleInIce)
     * 3. Any random direction (last resort, even if it means falling)
     * 
     * penguin: The AI penguin
     * returns thhe chosen direction
     */
    private Direction chooseAIDirection(Penguin penguin) {
        // Priority 1: Move toward food
        List<Direction> foodDirs = getDirectionsToFood(penguin);
        if (!foodDirs.isEmpty()) {
            return foodDirs.get(random.nextInt(foodDirs.size()));
        }
        
        // Priority 2: Move toward hazards except HoleINIce
        List<Direction> hazardDirs = getDirectionsToHazards(penguin, true);
        if (!hazardDirs.isEmpty()) {
            return hazardDirs.get(random.nextInt(hazardDirs.size()));
        }
        
        // Priority 3: Move in any random direction (last resort)
        Direction[] allDirs = Direction.values();
        return allDirs[random.nextInt(allDirs.length)];
    }
    
    /**
     * Find all directions that lead to food items.
     * Scans in each direction until hitting the edge or another object.
     * 
     * @param penguin The penguin to check from
     * @return List of directions that have food in them
     */
    private List<Direction> getDirectionsToFood(Penguin penguin) {
        List<Direction> directions = new ArrayList<>();
        Position pos = penguin.getPosition();
        
        // Check each of the 4 directions
        for (Direction dir : Direction.values()) {
            Position checkPos = pos.getNextPosition(dir);
            
            // Slide along this direction until we hit the edge
            while (checkPos.isValid()) {
                List<ITerrainObject> objects = terrain.getObjectsAt(checkPos);
                
                // Check if there's food at this position
                for (ITerrainObject obj : objects) {
                    if (obj instanceof Food) {
                        directions.add(dir);
                        break;  // Found food in this direction, no need to check further
                    }
                }
                
                // Move to next square in this direction
                checkPos = checkPos.getNextPosition(dir);
            }
        }
        
        return directions;
    }
    
    /**
     * Finds all directions that lead to hazards.
     * 
     * penguin: The penguin to check from
     * excludeHoleInIce: Whether to exclude HoleInIce from the search or not
     * returns the list of directions that have hazards in them
     */
    private List<Direction> getDirectionsToHazards(Penguin penguin, boolean excludeHoleInIce) {
        List<Direction> directions = new ArrayList<>();
        Position pos = penguin.getPosition();
        
        // Check each of the 4 directions
        for (Direction dir : Direction.values()) {
            Position checkPos = pos.getNextPosition(dir);
            
            // Slide along this direction until we hit the edge
            while (checkPos.isValid()) {
                List<ITerrainObject> objects = terrain.getObjectsAt(checkPos);
                
                // Check if there's a hazard at this position
                for (ITerrainObject obj : objects) {
                    if (obj instanceof IHazard) {
                        // Skip HoleInIce if requested so AI can avoid it
                        if (excludeHoleInIce && obj instanceof HoleInIce) {
                            continue;
                        }
                        
                        directions.add(dir);
                        break;  // Found hazard in this direction
                    }
                }
                
                checkPos = checkPos.getNextPosition(dir);
            }
        }
        
        return directions;
    }
    
    /**
     * Find a safe direction for Royal Penguin's single-step move.
     * Safe means: not into a hazard, not into another penguin, and not off the edge.
     * penguin: The Royal Penguin
     * returns a safe direction, or null if no safe direction exists
     */
    private Direction getSafeDirectionForRoyal(Penguin penguin) {
        //list of safe directions
        List<Direction> safeDirs = new ArrayList<>();
        Position pos = penguin.getPosition();
        
        // Check each direction for safety
        for (Direction dir : Direction.values()) {
            Position next = pos.getNextPosition(dir);
            
            // Must be within bounds
            if (next.isValid()) {
                List<ITerrainObject> objects = terrain.getObjectsAt(next);
                boolean safe = true;
                
                //Check if position contains a Hazard or a Penguin
                for (ITerrainObject obj : objects) {
                    if (obj instanceof IHazard || obj instanceof Penguin) {
                        safe = false;
                        break;
                    }
                }
                
                if (safe) {
                    safeDirs.add(dir);
                }
            }
        }
        
        // Return a random safe direction, or null if none exist
        if (safeDirs.isEmpty()) {
            return null;
        }
        return safeDirs.get(random.nextInt(safeDirs.size()));
    }
    
    /**
     * Convert a Direction enum to the name of the direction for display.
     * dir The direction
     * returns direction name
     */
    private String getDirectionName(Direction dir) {
        switch(dir) {
            case UP: return "UPWARDS";
            case DOWN: return "DOWNWARDS";
            case LEFT: return "to the LEFT";
            case RIGHT: return "to the RIGHT";
            default: return "";
        }
    }
    
    /**
     * Prompt the user for a Yes/No answer.
     * Keeps asking until a valid input (Y or N) is provided.
     * Input is not case sensitive
     * 
     * prompt: The question to ask the user
     * returns true if user answered Y, false if user answered N
     */
    private boolean getUserYesNo(String prompt) {
        // loop continues until user enters a valid input.
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase(); // trims the spaces and converts y,n to Y,N
            
            if (input.equals("Y")) {
                return true;
            }
            if (input.equals("N")) {
                return false;
            }
            
            // Invalid input, ask again
            System.out.println("Invalid input. Please enter Y or N.");
        }
    }
    
    /**
     * Prompts the user for a direction.
     * Keeps asking until a valid input (U/D/L/R) is provided.
     * Input is not case senswitive
     * 
     * prompt: the question to ask the user
     * returns: the chosen Direction
     */
    private Direction getUserDirection(String prompt) {
        // continues until user enters a valid input
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase(); // again trims and converts u,l,d,r to U,L,D,R if necessary
            
            switch(input) {
                case "U": return Direction.UP;
                case "D": return Direction.DOWN;
                case "L": return Direction.LEFT;
                case "R": return Direction.RIGHT;
                default: 
                    System.out.println("Invalid input. Please use U, D, L, or R.");
            }
        }
    }
    
    /**
     * Display the final game over screen with scoreboard.
     * Shows all penguins ranked by total food weight collected.
     * Displays each penguin's collected food items and total weight.
     */
    private void displayGameOver() {
        System.out.println("\n***** GAME OVER *****");
        System.out.println("***** SCOREBOARD FOR THE PENGUINS *****");
        
        // Sort penguins by total food weight (highest to lowest)
        List<Penguin> sortedPenguins = new ArrayList<>(penguins);
        //describes that each two penguins will be sorted according to their total food weight
        sortedPenguins.sort((p1, p2) -> 
            Integer.compare(p2.getTotalFoodWeight(), p1.getTotalFoodWeight()) 
        );  
        
        // Displays each penguin's score
        int currentRank = 1; // current rank assumes that first penguin of the list is the winner then adjusts the rank if it is not correct. (switches for penguins)
        for (int i = 0; i < sortedPenguins.size(); i++) {
            Penguin p = sortedPenguins.get(i);

            //Check for tie with previous penguin
            if (i > 0 && p.getTotalFoodWeight() < sortedPenguins.get(i - 1).getTotalFoodWeight()) {
                currentRank = i + 1;
            }

            String place = getPlaceSuffix(currentRank);
            String yourMarker = (p == playerPenguin) ? " (Your Penguin)" : ""; // adds "your penguin" string if the penguin is controlled by the user.
            
            System.out.println( place + " place: " + p.getName() + yourMarker);
            
            // Show collected food items
            if (p.getCollectedFood().isEmpty()) {
                System.out.println("|---> Food items: None");
                System.out.println("|---> Total weight: 0 units");
            } else {
                System.out.print("|---> Food items: ");
                
                // List all food items collected
                for (int j = 0; j < p.getCollectedFood().size(); j++) {
                    System.out.print(p.getCollectedFood().get(j).toString());
                    if (j < p.getCollectedFood().size() - 1) {
                        System.out.print(", ");
                    }
                }
                
                System.out.println();
                System.out.println("|---> Total weight: " + p.getTotalFoodWeight() + " units");
            }
        }
    }
    
    /**
     * Convert a numeric place (1, 2, 3) to ordinal suffix (1st, 2nd, 3rd).
    */
    private String getPlaceSuffix(int place) {
        switch(place) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            default: return place + "th";
        }
    }
}