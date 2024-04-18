package repositories;

import entities.KnownPoint;
import entities.UnknownPoint;
import services.InterpolationService;

import java.util.HashSet;
import java.util.Set;

public class LocationRepository {
    private static LocationRepository instance;
    private final Set<KnownPoint> knownPoints;
    private final Set<UnknownPoint> unknownPoints;

    private LocationRepository() {
        knownPoints = new HashSet<>();
        unknownPoints = new HashSet<>();
    }

    public static LocationRepository getInstance() {
        if (instance == null) {
            instance = new LocationRepository();
        }

        return instance;
    }

    public Set<KnownPoint> getKnownPoints() {
        return knownPoints;
    }

    public Set<UnknownPoint> getUnknownPoints() {
        return unknownPoints;
    }

    public void addKnownPoint(KnownPoint knownPoint) {
        knownPoints.add(knownPoint);
    }

    public void addUnknownPoint(UnknownPoint unknownPoint) {
        unknownPoints.add(unknownPoint);
    }
}
