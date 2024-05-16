package br.ufrn.dimap.repositories;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class LocationRepository {
    private static final LocationRepository instance = new LocationRepository();
    private final Set<KnownPoint> knownPoints;
    private final Set<UnknownPoint> unknownPoints;

    private LocationRepository() {
        knownPoints = new HashSet<>();
        unknownPoints = new HashSet<>();
    }

    public static LocationRepository getInstance() {
        return atomicInstance;
    }

    public Iterator<KnownPoint> getKnownPointsIterator() {
        return knownPoints.iterator();
    }

    public List<KnownPoint> getKnownPointsAsAList() {
        return knownPoints.stream().toList();
    }

    public Set<UnknownPoint> getUnknownPoints() {
        return unknownPoints.stream().collect(Collectors.toUnmodifiableSet());
    }

    public List<UnknownPoint> getUnknownPointsAsAList() {
        return unknownPoints.stream().toList();
    }

    public void addKnownPoint(KnownPoint knownPoint) {
        knownPoints.add(knownPoint);
    }

    public void addUnknownPoint(UnknownPoint unknownPoint) {
        unknownPoints.add(unknownPoint);
    }
}
