package com.example.homepc.walkinggroupapp.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Last GPS Location object that each user holds
 */
public class LastGpsLocation {
    private double lat;
    private double lng;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date timestamp;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LastGpsLocation{" +
                "lat=" + lat +
                ", lng='" + lng + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
