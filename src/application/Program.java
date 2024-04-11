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

        Runnable thirdTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(locations.size() / 3 * 2, locations.size()));

        runPlatformThreads(List.of(firstDatabase, secondDatabase, thirdDatabase));

        long checkpoint2 = System.currentTimeMillis();

        runPlatformThreads(List.of(firstTask, secondTask, thirdTask));

        long checkpoint3 = System.currentTimeMillis();

        System.out.printf("Time to read the database: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
        System.out.printf("Time to export the required locations: %.3fs%n", (checkpoint3 - checkpoint2) / 1e3);
        System.out.printf("Total time: %.3fs%n", (checkpoint3 - checkpoint1) / 1e3);
    }

    private static void runSerial(List<Runnable> runnables) {
        runnables.forEach(Runnable::run);
    }

    private static void runPlatformThreads(List<Runnable> runnables) throws InterruptedException {
        runPlatformThreads(runnables, Thread.MIN_PRIORITY);
    }

    private static void runVirtualThreads(List<Runnable> runnables) throws InterruptedException {
        runVirtualThreads(runnables, Thread.MIN_PRIORITY);
    }

    private static void runPlatformThreads(List<Runnable> runnables, int priority) throws InterruptedException {
        List<Thread> threads = runnables.stream().map(r -> Thread.ofPlatform().name("worker").start(r)).toList();

        for(Thread thread : threads){
            thread.setPriority(priority);
            thread.join();
        }
    }

    private static void runVirtualThreads(List<Runnable> runnables, int priority) throws InterruptedException {
        List<Thread> threads = runnables.stream().map(r -> Thread.ofVirtual().name("worker").start(r)).toList();

        for(Thread thread : threads){
            thread.setPriority(priority);
            thread.join();
        }
    }
}
