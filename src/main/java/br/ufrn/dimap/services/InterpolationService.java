package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static br.ufrn.dimap.repositories.LocationRepository.getInstance;
import static br.ufrn.dimap.util.Math.pow;

public class InterpolationService {
    public static void assignTemperatureToUnknownPoint(UnknownPoint unknownPoint) {
        double numerator = 0.0;
        double denominator = 0.0;

        Iterator<KnownPoint> knownPointsIterator = getInstance().getKnownPointsIterator();

        while(knownPointsIterator.hasNext()){
            KnownPoint knownPoint = knownPointsIterator.next();

//            3 is power parameter
            double dpp = pow(unknownPoint.getDistanceFromAnotherPoint(knownPoint), 3);

            numerator += knownPoint.getTemperature() / dpp;
            denominator += 1 / dpp;
        }

        unknownPoint.setTemperature(numerator / denominator);
    }

    public static void assignTemperatureToUnknownPoints(Collection<UnknownPoint> unknownPoints) {
        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPoint);
    }

    public static Callable<UnknownPoint> getInterpolationCallable(UnknownPoint unknownPoint) {
        return () -> {
            double numerator = 0.0;
            double denominator = 0.0;

            Iterator<KnownPoint> knownPointsIterator = getInstance().getKnownPointsIterator();

            while(knownPointsIterator.hasNext()){
                KnownPoint knownPoint = knownPointsIterator.next();

//            3 is power parameter
                double dpp = pow(unknownPoint.getDistanceFromAnotherPoint(knownPoint), 3);

                numerator += knownPoint.getTemperature() / dpp;
                denominator += 1 / dpp;
            }

            unknownPoint.setTemperature(numerator / denominator);

            return unknownPoint;
        };
    }
}
