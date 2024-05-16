package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;

import java.util.Collection;
import java.util.Iterator;

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

//    public static ImmutablePair<Double, Double> getNumeratorDenominatorSection(UnknownPoint unknownPoint, int start, int end) {
//        double numerator = 0.0;
//        double denominator = 0.0;
//
//        for(KnownPoint knownPoint : getInstance().getKnownPointsAsAList().subList(getInstance().getKnownPointsAsAList().size() / end * start, getInstance().getKnownPointsAsAList().size() / end * (start + 1))){
//            double dpp = pow(unknownPoint.getDistanceFromAnotherPoint(knownPoint), 3);
//
//            numerator += knownPoint.getTemperature() / dpp;
//            denominator += 1 / dpp;
//        }
//
//        return new ImmutablePair<>(numerator, denominator);
//    }
//
//    public static void assignTemperatureToUnknownPointThroughPlatformThreadsAdder(UnknownPoint unknownPoint) {
//        DoubleAdder numeratorAdder = new DoubleAdder();
//        DoubleAdder denominatorAdder = new DoubleAdder();
//
//        IntStream.range(0, getRuntime().availableProcessors()).forEach(i -> {
//            try {
//                Thread.ofPlatform().name("Task " + i).start(() -> {
//                    ImmutablePair<Double, Double> numeratorDenominator = getNumeratorDenominatorSection(unknownPoint, i, getRuntime().availableProcessors());
//
//                    numeratorAdder.add(numeratorDenominator.getLeft());
//                    denominatorAdder.add(numeratorDenominator.getRight());
//                }).join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        unknownPoint.setTemperature(numeratorAdder.doubleValue() / denominatorAdder.doubleValue());
//    }
//
//    public static void assignTemperatureToUnknownPointsThroughPlatformThreadsAdder(Collection<UnknownPoint> unknownPoints) {
//        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPointThroughPlatformThreadsAdder);
//    }
//
//    public static void assignTemperatureToUnknownPointThroughVirtualThreadsAdder(UnknownPoint unknownPoint) {
//        DoubleAdder numeratorAdder = new DoubleAdder();
//        DoubleAdder denominatorAdder = new DoubleAdder();
//
//        IntStream.range(0, getRuntime().availableProcessors()).forEach(i -> {
//            try {
//                Thread.ofVirtual().name("Task " + i).start(() -> {
//                    System.out.println(i);
//
//                    ImmutablePair<Double, Double> numeratorDenominator = getNumeratorDenominatorSection(unknownPoint, i, getRuntime().availableProcessors());
//
//                    numeratorAdder.add(numeratorDenominator.getLeft());
//                    denominatorAdder.add(numeratorDenominator.getRight());
//                }).join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        unknownPoint.setTemperature(numeratorAdder.doubleValue() / denominatorAdder.doubleValue());
//    }
//
//    public static void assignTemperatureToUnknownPointsThroughVirtualThreadsAdder(Collection<UnknownPoint> unknownPoints) {
//        unknownPoints.forEach(InterpolationService::assignTemperatureToUnknownPointThroughVirtualThreadsAdder);
//    }
}
