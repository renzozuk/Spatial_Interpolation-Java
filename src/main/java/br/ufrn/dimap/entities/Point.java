package br.ufrn.dimap.entities;

import java.util.Objects;

public abstract class Point {
    private final double latitude;
    private final double longitude;
    private double temperature;

    public Point(double latitude, double longitude) {
        if(latitude < -90.00 || latitude > 90.00){
            throw new IllegalArgumentException("Invalid value for latitude. Note that the latitude value must be between -90 and 90.");
        }

        if(longitude < -180.00 || longitude > 180.00){
            throw new IllegalArgumentException("Invalid value for longitude. Note that the longitude value must be between -180 and 180.");
        }

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(double latitude, double longitude, double temperature) {
        this(latitude, longitude);
        this.temperature = temperature;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        if(this instanceof UnknownPoint){
            this.temperature = temperature;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return latitude == point.latitude && longitude == point.longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    public double getDistanceFromAnotherPoint(Point point){
        if(this == point){
            return 0.0;
        }

        double latitudeDistance = point.getLatitude() * Math.PI / 180 - latitude * Math.PI / 180;
        double longitudeDistance = point.getLongitude() * Math.PI / 180 - longitude * Math.PI / 180;

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2) +
                Math.cos(latitude * Math.PI / 180) * Math.cos(point.getLatitude() * Math.PI / 180) *
                Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
//        6378.137 = earth radius
        return 6378.137 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
