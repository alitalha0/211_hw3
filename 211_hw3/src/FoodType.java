public enum FoodType {
    KRILL, CRUSTACEAN, ANCHOVY, SQUID, MACKEREL;

    public String getShortHand() {
        switch (this) {
            case KRILL: return "Kr";
            case CRUSTACEAN: return "Cr";
            case ANCHOVY: return "An";
            case SQUID: return "Sq";
            case MACKEREL: return "Ma";
            
            default: return "error in food";

        }
    }
}
