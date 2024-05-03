package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.util.Collection;

public class InterpolationService {
    public static void assignTemperatureToUnknownPoint(UnknownPoint unknownPoint) {
        double numerator = 0.0;
        double denominator = 0.0;
        double powerParameter = 2.5;

        for(KnownPoint knownPoint : LocationRepository.getInstance().getKnownPoints()){
            numerator += knownPoint.getTemperature() / Math.pow(knownPoint.getDistanceFromAnotherPoint(unknownPoint), powerParameter);
            denominator += 1 / Math.pow(knownPoint.getDistanceFromAnotherPoint(unknownPoint), powerParameter);
        }

        unknownPoint.setTemperature(numerator / denominator);
    }

    public static void assignTemperatureToUnknownPoints(Collection<UnknownPoint> unknownPoints) {
        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPoint);
    }
}
