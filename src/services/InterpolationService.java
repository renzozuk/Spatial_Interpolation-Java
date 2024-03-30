package services;

import entities.Interpolation;
import entities.Point;
import util.MomentConverter;
import util.MomentIterator;

import java.time.Instant;
import java.util.List;

public class InterpolationService {
    public static void showInterpolation(MomentIterator iterator, Point location){
        while(iterator.hasNext()){
            System.out.println(new Interpolation(location, iterator.next().toInstant()));
        }
    }

    public static void showInterpolation(MomentIterator iterator, List<Point> locations){
        while(iterator.hasNext()){
            for(Point location : locations){
                System.out.println(new Interpolation(location, iterator.next().toInstant()));
            }
        }
    }

    public static void exportInterpolation(Instant moment, Point location) {
        FileManagementService.exportInterpolation(new Interpolation(location, moment));
    }

    public static void exportInterpolation(String moment, Point location) {
        exportInterpolation(MomentConverter.mapToInstant(moment), location);
    }

    public static void exportInterpolation(MomentIterator iterator, Point location) {
        while(iterator.hasNext()){
            FileManagementService.exportInterpolation(new Interpolation(location, iterator.next().toInstant()));
        }
    }

    public static void exportInterpolation(Instant moment, List<Point> locations) {
        for(Point location : locations) {
            FileManagementService.exportInterpolation(new Interpolation(location, moment));
        }
    }

    public static void exportInterpolation(String moment, List<Point> locations) {
        exportInterpolation(MomentConverter.mapToInstant(moment), locations);
    }

    public static void exportInterpolation(MomentIterator iterator, List<Point> locations) {
        while(iterator.hasNext()){
            exportInterpolation(iterator.next().toInstant(), locations);
        }
    }
}
