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
 * This class handles:
 * - Game initialization (generating penguins, hazards, and food)
 * - Turn management (player and AI turns)
 * - User input handling
 * - Game flow and win condition
 * 
 * @author Your Name
 */
public class GameController {
    // Core game components
    private IcyTerrain terrain;           // The 10x10 icy grid
    private List<Penguin> penguins;       // All 3 penguins in the game
    private Penguin playerPenguin;        // The penguin assigned to the player
    
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
     * Handles the complete game flow from initialization to game over.
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
     * 1. Create the terrain
     * 2. Generate 3 penguins and assign one to the player
     * 3. Generate 15 hazards
     * 4. Generate 20 food items
     */
    private void initializeGame() {
        terrain = new IcyTerrain();
        
        // Generate 3 penguins and place them on edge squares
        penguins = generatePenguins();
        
        // Randomly assign one penguin to the player
        playerPenguin = penguins.get(random.nextInt(3));
        
        // Place hazards on the grid
        generateHazards();
        
        // Place food items on the grid
        generateFood();
    }
    
    /**
     * Generate 3 penguins with random types and place them on edge squares.
     * Each penguin has an equal chance of being any of the 4 types.
     * No two penguins can occupy the same square.
     * 
     * @return List of 3 generated penguins
     */
    private List<Penguin> generatePenguins() {
        List<Penguin> penguins = new ArrayList<>();
        PenguinType[] types = PenguinType.values();
        
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
     * Each food type has equal probability.
     * Each weight (1-5) has equal probability.
     * Food items cannot be placed where penguins or hazards exist.
     */
    private void generateFood() {
        FoodType[] foodTypes = FoodType.values();
        
        // Create exactly 20 food items
        for (int i = 0; i < 20; i++) {
            // Randomly select food type and weight
            FoodType type = foodTypes[random.nextInt(foodTypes.length)];
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
     * Edge squares are those with row=0, row=9, col=0, or col=9.
     * 
     * @return A random edge position
     */
    private Position getRandomEdgePosition() {
        int side = random.nextInt(4);
        switch(side) {
            case 0: return new Position(0, random.nextInt(10));          // Top edge
            case 1: return new Position(9, random.nextInt(10));          // Bottom edge
            case 2: return new Position(random.nextInt(10), 0);          // Left edge
            case 3: return new Position(random.nextInt(10), 9);          // Right edge
            default: return new Position(0, 0);
        }
    }
    
    /**
     * Get a random position anywhere on the 10x10 grid.
     * 
     * @return A random position
     */
    private Position getRandomPosition() {
        return new Position(random.nextInt(10), random.nextInt(10));
    }
    
    /**
     * Check if a position is available for placing food.
     * Food can be placed only if there are no penguins or hazards at that position.
     * Food items can coexist with other food items.
     * 
     * @param pos The position to check
     * @return true if the position is available for food, false otherwise
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
            String marker = (penguin == playerPenguin) ? " ---> YOUR PENGUIN" : "";
            
            System.out.println("- Penguin " + penguin.getName().charAt(1) + 
                             " (" + penguin.getName() + "): " + 
                             penguin.getPenguinTypeName() + marker);
        }
    }
    
    /**
     * Execute a single turn for a penguin.
     * Handles both player turns (with user input) and AI turns (automated).
     * 
     * @param penguin The penguin whose turn it is
     * @param turnNumber The current turn number (1-4)
     */
    private void playTurn(Penguin penguin, int turnNumber) {
        // Skip turn if penguin has been removed from the game
        if (penguin.isRemoved()) {
            return;
        }
        
        // Display turn header
        String turnLabel = (penguin == playerPenguin) ? 
            "*** Turn " + turnNumber + " " + penguin.getName() + " (Your Penguin):" :
            "*** Turn " + turnNumber + " " + penguin.getName() + ":";
        System.out.println("\n" + turnLabel);
        
        // Check if penguin is stunned (skips turn)
        if (penguin.isStunned()) {
            System.out.println(penguin.getName() + " is stunned and skips this turn.");
            penguin.setStunned(false);  // Remove stun for next turn
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
     * 
     * @param penguin The player's penguin
     */
    private void playPlayerTurn(Penguin penguin) {
        boolean useSpecial = false;
        
        // Ask if player wants to use special action (if not already used)
        if (!penguin.isSpecialActionUsed()) {
            useSpecial = getUserYesNo("Will " + penguin.getName() + 
                                     " use its special action? Answer with Y or N --> ");
        }
        
        // Ask for movement direction
        Direction dir = getUserDirection("Which direction will " + penguin.getName() + 
                                        " move? Answer with U (Up), D (Down), L (Left), R (Right) --> ");
        
        // Use special action if player chose to
        if (useSpecial) {
            announceSpecialAction(penguin, dir, true);
            penguin.useSpecialAction(dir, terrain);
            
            // Royal Penguin's special action is just a single step, no slide needed
            if (penguin instanceof RoyalPenguin) {
                handlePositionAfterMove(penguin);
                return;  // Don't slide after using Royal ability
            }
        }
        
        // Execute the normal slide movement
        penguin.slide(dir, terrain);
        
        // Announce any food collected or hazards encountered
        handlePositionAfterMove(penguin);
    }
    
    /**
     * Handle an AI penguin's turn with automated decision making.
     * 
     * AI behavior:
     * - 30% chance to use special action (except Rockhopper which auto-uses when needed)
     * - Prioritizes moving toward food
     * - Otherwise moves toward hazards (except HoleInIce)
     * - Last resort: moves in any direction (even falling into water)
     * 
     * @param penguin The AI-controlled penguin
     */
    private void playAITurn(Penguin penguin) {
        boolean useSpecial = false;
        Direction chosenDir = null;
        
        // Special case: Rockhopper automatically uses ability when moving toward hazards
        if (penguin instanceof RockhopperPenguin && !penguin.isSpecialActionUsed()) {
            List<Direction> hazardDirs = getDirectionsToHazards(penguin, true);
            
            if (!hazardDirs.isEmpty()) {
                // Rockhopper will automatically use jump ability
                useSpecial = true;
                chosenDir = hazardDirs.get(random.nextInt(hazardDirs.size()));
                System.out.println(penguin.getName() + " will automatically USE its special action.");
            }
        } 
        // For other penguins: 30% chance to use special action
        else if (!penguin.isSpecialActionUsed() && random.nextDouble() < 0.3) {
            useSpecial = true;
            
            // Royal Penguin announces differently
            if (penguin instanceof RoyalPenguin) {
                System.out.println(penguin.getName() + " chooses to USE its special action.");
            } else {
                // King and Emperor just use it during their slide
                System.out.println(penguin.getName() + " chooses to use its special action.");
            }
        } else {
            System.out.println(penguin.getName() + " does NOT to use its special action.");
        }
        
        // Choose direction if not already chosen (by Rockhopper logic)
        if (chosenDir == null) {
            chosenDir = chooseAIDirection(penguin);
        }
        
        // Announce the chosen direction
        System.out.println(penguin.getName() + " chooses to move " + getDirectionName(chosenDir) + ".");
        
        // Execute special action if needed
        if (useSpecial) {
            announceSpecialAction(penguin, chosenDir, false);
            penguin.useSpecialAction(chosenDir, terrain);
            
            // Royal Penguin doesn't slide after using ability
            if (penguin instanceof RoyalPenguin) {
                handlePositionAfterMove(penguin);
                return;
            }
        }
        
        // Execute the normal slide
        penguin.slide(chosenDir, terrain);
        
        // Handle any results of the movement
        handlePositionAfterMove(penguin);
    }
    
    /**
     * Announce what happens when a penguin uses its special action.
     * Different messages for different penguin types.
     * 
     * @param penguin The penguin using its special action
     * @param dir The direction of movement
     * @param isPlayer Whether this is the player's penguin
     */
    private void announceSpecialAction(Penguin penguin, Direction dir, boolean isPlayer) {
        if (penguin instanceof KingPenguin) {
            // King can stop at 5th square
            // Announcement happens after the slide completes
        } else if (penguin instanceof EmperorPenguin) {
            // Emperor can stop at 3rd square
            // Announcement happens after the slide completes
        } else if (penguin instanceof RoyalPenguin) {
            // Royal moves one square
            if (!isPlayer) {
                // For AI, try to find a safe direction
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
        // Check if penguin fell off the grid
        if (penguin.isRemoved()) {
            return;  // Will be announced in playTurn()
        }
        
        // Check if penguin collected any food
        List<Food> collectedFood = penguin.getCollectedFood();
        if (!collectedFood.isEmpty()) {
            Food lastFood = collectedFood.get(collectedFood.size() - 1);
            
            // FIX: Convert "KRILL" to "Krill" for display
            String foodTypeStr = lastFood.getType().toString();
            String foodName = foodTypeStr.charAt(0) + foodTypeStr.substring(1).toLowerCase();
            
            System.out.println(penguin.getName() + " takes the " + 
                             foodName + " on the ground. (Weight " + 
                             lastFood.getWeight() + " units)");
        }
    }
    
    /**
     * Choose the best direction for an AI penguin to move.
     * 
     * Priority system:
     * 1. Directions that lead to food (highest priority)
     * 2. Directions that lead to hazards (except HoleInIce)
     * 3. Any random direction (last resort, even if it means falling)
     * 
     * @param penguin The AI penguin
     * @return The chosen direction
     */
    private Direction chooseAIDirection(Penguin penguin) {
        // Priority 1: Move toward food
        List<Direction> foodDirs = getDirectionsToFood(penguin);
        if (!foodDirs.isEmpty()) {
            return foodDirs.get(random.nextInt(foodDirs.size()));
        }
        
        // Priority 2: Move toward hazards (excluding HoleInIce)
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
     * Find all directions that lead to hazards.
     * 
     * @param penguin The penguin to check from
     * @param excludeHoleInIce Whether to exclude HoleInIce from the search
     * @return List of directions that have hazards in them
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
                        // Skip HoleInIce if requested (AI avoids these)
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
     * * @param penguin The Royal Penguin
     * @return A safe direction, or null if no safe direction exists
     */
    private Direction getSafeDirectionForRoyal(Penguin penguin) {
        List<Direction> safeDirs = new ArrayList<>();
        Position pos = penguin.getPosition();
        
        // Check each direction for safety
        for (Direction dir : Direction.values()) {
            Position next = pos.getNextPosition(dir);
            
            // Must be within bounds
            if (next.isValid()) {
                List<ITerrainObject> objects = terrain.getObjectsAt(next);
                boolean safe = true;
                
                //Check if position contains a Hazard OR a Penguin
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
     * Convert a Direction enum to a human-readable string.
     * Used for displaying messages to the player.
     * 
     * @param dir The direction
     * @return Human-readable direction name
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
     * Input is case-insensitive.
     * 
     * @param prompt The question to ask the user
     * @return true if user answered Y, false if user answered N
     */
    private boolean getUserYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            
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
     * Prompt the user for a direction.
     * Keeps asking until a valid input (U/D/L/R) is provided.
     * Input is case-insensitive.
     * 
     * @param prompt The question to ask the user
     * @return The chosen Direction
     */
    private Direction getUserDirection(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            
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
        sortedPenguins.sort((p1, p2) -> 
            Integer.compare(p2.getTotalFoodWeight(), p1.getTotalFoodWeight())
        );  
        
        // Display each penguin's score
        int currentRank = 1;
        for (int i = 0; i < sortedPenguins.size(); i++) {
            Penguin p = sortedPenguins.get(i);

            //Check for tie with previous penguin
            if (i > 0 && p.getTotalFoodWeight() < sortedPenguins.get(i - 1).getTotalFoodWeight()) {
                currentRank = i + 1;
            }

            String place = getPlaceSuffix(currentRank);
            String yourMarker = (p == playerPenguin) ? " (Your Penguin)" : "";
            
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
     * 
     * @param place The numeric place
     * @return The place with ordinal suffix
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