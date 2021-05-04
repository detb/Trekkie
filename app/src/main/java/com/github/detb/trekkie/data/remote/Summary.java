package com.github.detb.trekkie.data.remote;
public class Summary{
    public int value;
    public double distance;
    public double amount;
    public double duration;

    public double getWayTypeAmount()
    {
        return amount;
    }

    public String getWayTypeAmountPercentage()
    {
        return (float)amount + " %";
    }

    public String getWayTypeAsString()
    {
        switch (value) {
            case 0: return "Unknown";
            case 1:	return "State Road";
            case 2:	return "Road";
            case 3:	return "Street";
            case 4:	return "Path";
            case 5:	return "Track";
            case 6:	return "Cycleway";
            case 7:	return "Footway";
            case 8:	return "Steps";
            case 9: return "Ferry";
            case 10: return "Construction";
        }
        return null;
    }

    public String getSurfaceTypeAsString() {
        switch (value) {
            case 0:	return "Unknown";
            case 1:	return "Paved";
            case 2:	return "Unpaved";
            case 3:	return "Asphalt";
            case 4:	return "Concrete";
            case 5:	return "Cobblestone";
            case 6:	return "Metal";
            case 7:	return "Wood";
            case 8:	return "Compacted Gravel";
            case 9:	return "Fine Gravel";
            case 10: return "Gravel";
            case 11: return "Dirt";
            case 12: return "Ground";
            case 13: return "Ice";
            case 14: return "Paving Stones";
            case 15: return "Sand";
            case 16: return "Woodchips";
            case 17: return "Grass";
            case 18: return "Grass Paver";
        }
        return null;
    }
    @Override
    public String toString() {
        return "Summary{" +
                "value=" + getWayTypeAsString() +
                ", distance=" + distance +
                ", amount=" + amount +
                ", duration=" + duration +
                '}';
    }


    public String getColorString() {
        switch (value) {
            case 0: return "#4863F4";
            case 1:	return "#4863F4";
            case 2:	return "#232323";
            case 3:	return "#6C6C6C";
            case 4:	return "#805312";
            case 5:	return "#FF470E";
            case 6:	return "#4863F4";
            case 7:	return "#005E04";
            case 8:	return "#4863F4";
            case 9: return "#4863F4";
            case 10: return "#4863F4";
        }
        return null;
    }
}
