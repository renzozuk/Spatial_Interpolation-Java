package br.ufrn.dimap.entities;

import java.util.Objects;

import static br.ufrn.dimap.util.Math.DEGREES_TO_RADIANS;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

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

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", temperature=" + temperature +
                '}';
    }

    public double getDistanceFromAnotherPoint(Point point){
        if(this == point){
            return 0.0;
        }

        double dLat = (point.getLatitude() - latitude) * DEGREES_TO_RADIANS;
        double dLon = (point.getLongitude() - longitude) * DEGREES_TO_RADIANS;

        double a = sin(dLat / 2.0) * sin(dLat / 2.0) +
                cos(latitude * DEGREES_TO_RADIANS) * cos(point.getLatitude() * DEGREES_TO_RADIANS) *
                sin(dLon / 2.0) * sin(dLon / 2.0);

        return 12742.0 * atan2(sqrt(a), sqrt(1 - a));
    }
}
