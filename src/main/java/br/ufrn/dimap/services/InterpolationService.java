package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.util.Collection;
import java.util.Iterator;

import static br.ufrn.dimap.util.Math.pow;

public class InterpolationService {
    public static void assignTemperatureToUnknownPoint(UnknownPoint unknownPoint) {
        double numerator = 0.0;
        double denominator = 0.0;

        Iterator<KnownPoint> knownPointsIterator = LocationRepository.getInstance().getKnownPointsIterator();

        while(knownPointsIterator.hasNext()){
            KnownPoint knownPoint = knownPointsIterator.next();

//            3 is power parameter
            double dpp = pow(unknownPoint.getDistanceFromAnotherPoint(knownPoint), 3);

            numerator += knownPoint.getTemperature() / dpp;
            denominator += 1 / dpp;
        }

        unknownPoint.setTemperature(numerator / denominator);
    }

    public static void assignTemperatureToUnknownPointThroughList(UnknownPoint unknownPoint) {
        double numerator = 0.0;
        double denominator = 0.0;

        for(KnownPoint knownPoint : LocationRepository.getInstance().getKnownPointsAsAList()){
            double dpp = pow(unknownPoint.getDistanceFromAnotherPoint(knownPoint), 3);

            numerator += knownPoint.getTemperature() / dpp;
            denominator += 1 / dpp;
        }

        unknownPoint.setTemperature(numerator / denominator);
    }

    public static void assignTemperatureToUnknownPoints(Collection<UnknownPoint> unknownPoints) {
        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPoint);
    }

    public static void assignTemperatureToUnknownPointThroughList(Collection<UnknownPoint> unknownPoints) {
        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPointThroughList);
    }
}
