package com.github.detb.trekkie.data; 
public class Summary{
    public int value;
    public double distance;
    public double amount;
    public double duration;

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
            case 10: return  "Construction";
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
}
