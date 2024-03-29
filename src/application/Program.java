package application;

import entities.Point;
import services.FileManagementService;
import services.InterpolationService;
import util.MomentIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Program {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        List<Point> locations = FileManagementService.importLocations();

        Runnable firstDatabase = () -> {
            try {
                FileManagementService.importDatabase("Brazil - 2021");
                FileManagementService.importDatabase("Uruguay - 2021");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable secondDatabase = () -> {
            try {
                FileManagementService.importDatabase("Brazil - 2022");
                FileManagementService.importDatabase("Uruguay - 2022");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable thirdDatabase = () -> {
            try {
                FileManagementService.importDatabase("Brazil - 2023");
                FileManagementService.importDatabase("Uruguay - 2023");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable firstTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(0, locations.size() / 3));

        Runnable secondTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(locations.size() / 3, locations.size() / 3 * 2));

        Runnable thirdTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(locations.size() / 3 * 2, locations.size())));

        Thread.Builder builder1 = Thread.ofVirtual().name("worker-", 0);
        Thread.Builder builder2 = Thread.ofVirtual().name("worker-", 1);
        Thread.Builder builder3 = Thread.ofVirtual().name("worker-", 2);

        Thread db1 = builder1.start(firstDatabase);
        Thread db2 = builder2.start(secondDatabase);
        Thread db3 = builder3.start(thirdDatabase);

        db1.join();
        db2.join();
        db3.join();

        long checkpoint2 = System.currentTimeMillis();

        Thread t1 = builder1.start(firstTask);
        Thread t2 = builder2.start(secondTask);
        Thread t3 = builder3.start(thirdTask);

        t1.join();
        t2.join();
        t3.join();

        long checkpoint3 = System.currentTimeMillis();

        System.out.printf("Time to read the database: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
        System.out.printf("Time to export the required locations: %.3fs%n", (checkpoint3 - checkpoint2) / 1e3);
        System.out.printf("Total time: %.3fs%n", (checkpoint3 - checkpoint1) / 1e3);
    }
}
