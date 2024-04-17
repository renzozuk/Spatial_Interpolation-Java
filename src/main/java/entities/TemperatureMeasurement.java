package entities;

import java.util.Objects;

public class TemperatureMeasurement {
    private Point point;
    private double temperature;

    public TemperatureMeasurement(Point point, double temperature) {
        this.point = point;
        this.temperature = temperature;
    }

    public TemperatureMeasurement(/*String placeName, */double latitude, double longitude, double temperature) {
        this(new Point(/*placeName, */latitude, longitude), temperature);
    }

    public Point getPoint() {
        return point;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TemperatureMeasurement that = (TemperatureMeasurement) object;
        return Double.compare(temperature, that.temperature) == 0 && Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, temperature);
    }

    @Override
    public String toString() {
        return point + "[Temperature: " + String.format("%.1f", temperature) + "ºC]";
    }
}
