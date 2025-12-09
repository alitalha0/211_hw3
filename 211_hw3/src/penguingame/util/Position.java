package penguingame.util;
import penguingame.enums.Direction;

//Position class that handles the position of the squares of 10*10 grid
public class Position {
    private final int row;
    private final int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;

    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    
    // grid is not valid if it is out of bounds of the 10*10 grid
    public boolean isValid(){
        return (row >= 0 && row < 10 && col >= 0 && col < 10);
    }

    // gets the next position towards the new direction
    public Position getNextPosition(Direction dir) {
        switch (dir) {
            case UP: return new Position(this.row - 1, this. col); 
                
            case DOWN: return new Position(this.row + 1, this.col );
                
            case LEFT: return new Position(this.row, this.col - 1);
                
            case RIGHT: return new Position(this.row, this.col + 1);
                
            default: return this;
        }
    }

    // checks whether the square is at the edge of the 10*10 grid
    public boolean isEdge() {
        return row == 0 || row == 9 || col == 0 || col == 9;
    }

    // two position is equal if their rows and columns are equal
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }
        Position objPosition = (Position) obj;
        return this.row == objPosition.row && this.col == objPosition.col;
    }
}
