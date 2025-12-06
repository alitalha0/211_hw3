public interface ISlidable {
    boolean canSlide();
    void slide(Direction dir, IcyTerrain terrain);
}
