package br.ufrn.dimap.repositories;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;

import java.util.*;
import java.util.stream.Collectors;

public class LocationRepository {
    private static final LocationRepository instance = new LocationRepository();
    private final Set<KnownPoint> knownPoints;
    private final Set<UnknownPoint> unknownPoints;

    private LocationRepository() {
        knownPoints = Collections.synchronizedSet(new HashSet<>());
        unknownPoints = Collections.synchronizedSet(new HashSet<>());
    }

    public static LocationRepository getInstance() {
        return instance;
    }

    public Iterator<KnownPoint> getKnownPointsIterator() {
        return knownPoints.iterator();
    }

    public List<KnownPoint> getKnownPointsAsList() {
        return knownPoints.stream().toList();
    }

    public Set<UnknownPoint> getUnknownPoints() {
        return unknownPoints.stream().collect(Collectors.toUnmodifiableSet());
    }

    public List<UnknownPoint> getUnknownPointsAsList() {
        return unknownPoints.stream().toList();
    }

    public void addKnownPoint(KnownPoint knownPoint) {
        knownPoints.add(knownPoint);
    }

    public void addUnknownPoint(UnknownPoint unknownPoint) {
        unknownPoints.add(unknownPoint);
    }
}
