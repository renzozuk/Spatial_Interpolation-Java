package services;

import entities.Interpolation;
import entities.Point;
import util.MomentConverter;
import util.MomentIterator;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;

public class InterpolationService {
    public static void showInterpolation(MomentIterator iterator, Point location){
        while(iterator.hasNext()){
            Calendar currentMoment = iterator.next();

            System.out.println(new Interpolation(location, currentMoment.toInstant()));
        }
    }

    public static void showInterpolation(MomentIterator iterator, List<Point> locations){
        while(iterator.hasNext()){
            Calendar currentMoment = iterator.next();

            for(Point location : locations){
                System.out.println(new Interpolation(location, currentMoment.toInstant()));
            }
        }
    }

    public static void exportInterpolation(Instant moment, Point location) {
        Interpolation interpolation = new Interpolation(location, moment);

        FileManagementService.exportInterpolation(interpolation);
//        System.out.println("Interpolation [" + interpolation.getMainTemperatureMeasurement().getPoint().getName() + "] [" + interpolation.getMoment() + "] exported successfully.");
    }

    public static void exportInterpolation(String moment, Point location) {
        exportInterpolation(MomentConverter.mapToInstant(moment), location);
    }

    public static void exportInterpolation(MomentIterator iterator, Point location) {
        while(iterator.hasNext()){
            Calendar currentMoment = iterator.next();

            Interpolation interpolation = new Interpolation(location, currentMoment.toInstant());

            FileManagementService.exportInterpolation(interpolation);
//            System.out.println("Interpolation [" + interpolation.getMainTemperatureMeasurement().getPoint().getName() + "] [" + interpolation.getMoment() + "] exported successfully.");
        }
    }

    public static void exportInterpolation(Instant moment, List<Point> locations) {
        for(Point location : locations) {
            Interpolation interpolation = new Interpolation(location, moment);

            FileManagementService.exportInterpolation(interpolation);

//            System.out.println("Interpolation [" + interpolation.getMainTemperatureMeasurement().getPoint().getName() + "] [" + interpolation.getMoment() + "] exported successfully.");
        }
    }

    public static void exportInterpolation(String moment, List<Point> locations) {
        exportInterpolation(MomentConverter.mapToInstant(moment), locations);
    }

    public static void exportInterpolation(MomentIterator iterator, List<Point> locations) {
        while(iterator.hasNext()){
            Calendar currentMoment = iterator.next();

            exportInterpolation(currentMoment.toInstant(), locations);
        }
    }
}
