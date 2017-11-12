package com.example.handlers.parkingslots;

/**
 * Created by himanshu on 12/11/17.
 */

public class Parking {
    private String id;
    private String name;
    private String lat;
    private String lon;
    private String total;
    private String four_avail;
    private String three_avail;
    private String two_avail;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getTotal() {
        return total;
    }

    public String getFour_avail() {
        return four_avail;
    }

    public String getThree_avail() {
        return three_avail;
    }

    public String getTwo_avail() {
        return two_avail;
    }

    public Parking(String id, String name, String lat, String lon, String total, String four_avail, String three_avail, String two_avail) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.total = total;
        this.four_avail = four_avail;
        this.three_avail = three_avail;
        this.two_avail = two_avail;
    }
}
