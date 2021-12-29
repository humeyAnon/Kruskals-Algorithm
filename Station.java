
// Station class - Stores all relevant information about the stations

import java.util.ArrayList;

public class Station {

    private float x, y;
    private int stationID;
    private ArrayList<HotSpot> servicedHotspots;

    public Station(float x, float y, ArrayList<HotSpot> servicedHotspots, int stationID) {
        this.x = x;
        this.y = y;
        this.servicedHotspots = servicedHotspots;
        this.stationID = stationID;
    }

    public String getHotSpots() {
        String hotSpots = "";

        for(HotSpot h : servicedHotspots) {

            hotSpots = hotSpots.concat(Integer.toString(h.getHotSpotID()));

            if(servicedHotspots.indexOf(h) != (servicedHotspots.size() - 1)) {

                hotSpots = hotSpots.concat(",");

            }
        }
        return hotSpots;
    }
}
