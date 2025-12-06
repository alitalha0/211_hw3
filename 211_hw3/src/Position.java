public class Position {
    private final int row;
    private final int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;

    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    

    public boolean isValid(){
        return (row >= 0 && row < 10 && col >= 0 && col < 10);
    }

    public Position getNextPosition(Direction dir) {
        switch (dir) {
            case UP: return new Position(this.row + 1, this. col); 
                
            case DOWN: return new Position(this.row - 1, this.col );
                
            case LEFT: return new Position(this.row, this.col - 1);
                
            case RIGHT: return new Position(this.row, this.col + 1);
                
            default: return this;
        }
    }

    public boolean isEdge() {
        return row == 0 || row == 9 || col == 0 || col == 9;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }
        Position objPosition = (Position) obj;
        return this.row == objPosition.row && this.col == objPosition.col;
    }
}
