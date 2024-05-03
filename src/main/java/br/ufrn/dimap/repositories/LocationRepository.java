package br.ufrn.dimap.repositories;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class LocationRepository {
    private static LocationRepository instance;
    private static AtomicReference<LocationRepository> atomicInstance;
    private final Set<KnownPoint> knownPoints;
    private final Set<UnknownPoint> unknownPoints;

    private LocationRepository() {
        knownPoints = new HashSet<>();
        unknownPoints = new HashSet<>();
    }

    private static LocationRepository getNotNullInstance() {
        if (instance != null) {
            return instance;
        }

        if (atomicInstance != null) {
            return atomicInstance.get();
        }

        return null;
    }

    public static LocationRepository getInstance() {
        if (instance == null && atomicInstance == null) {
            instance = new LocationRepository();
        }

        return getNotNullInstance();
    }

    public static LocationRepository getAtomicInstance() {
        if (instance == null && atomicInstance == null) {
            atomicInstance = new AtomicReference<>(new LocationRepository());
        }

        return getNotNullInstance();
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
