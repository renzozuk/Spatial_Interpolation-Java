package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.util.Collection;
import java.util.Iterator;

public class InterpolationService {
    public static void assignTemperatureToUnknownPoint(UnknownPoint unknownPoint) {
        double numerator = 0.0;
        double denominator = 0.0;
        double powerParameter = 2.5;

        Iterator<KnownPoint> knownPointsIterator = LocationRepository.getInstance().getKnownPointsIterator();

        while(knownPointsIterator.hasNext()){
            KnownPoint knownPoint = knownPointsIterator.next();
            numerator += knownPoint.getTemperature() / Math.pow(knownPoint.getDistanceFromAnotherPoint(unknownPoint), powerParameter);
            denominator += 1 / Math.pow(knownPoint.getDistanceFromAnotherPoint(unknownPoint), powerParameter);
        }

        unknownPoint.setTemperature(numerator / denominator);
    }

    public static void assignTemperatureToUnknownPoints(Collection<UnknownPoint> unknownPoints) {
        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPoint);
    }
}
