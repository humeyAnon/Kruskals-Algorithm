
// Hotspot class - Stores all relevant information about the hotspots
//                 That have been read in from the input file

public class HotSpot {

    int hotSpotID;
    float x, y;

    public HotSpot(int hotSpotID, float x, float y) {

        this.hotSpotID = hotSpotID;
        this.x = x;
        this.y = y;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHotSpotID() {
        return hotSpotID;
    }

}
