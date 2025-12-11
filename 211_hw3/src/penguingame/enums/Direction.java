package penguingame.enums;

//enum for direction that user is going to enter.
public enum Direction {
    UP, DOWN, LEFT, RIGHT;
    
    //this function is added to get the opposite direction when a penguin hits a sealion
    public Direction getOpposite() {
        switch(this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return this;
        }
    }
}
