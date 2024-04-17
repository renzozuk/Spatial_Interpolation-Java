package services;

import entities.Point;
import util.MomentIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ExecutionService {
    public static void runSequential(List<Runnable> runnables) {
        runnables.forEach(Runnable::run);
    }

    public static void runPlatformThreads(List<Runnable> runnables) throws InterruptedException {
        runPlatformThreads(runnables, Thread.MIN_PRIORITY);
    }

    public static void runVirtualThreads(List<Runnable> runnables) throws InterruptedException {
        runVirtualThreads(runnables, Thread.MIN_PRIORITY);
    }

    public static void runPlatformThreads(List<Runnable> runnables, int priority) throws InterruptedException {
        List<Thread> threads = runnables.stream().map(r -> Thread.ofPlatform().name(r.getClass().getSimpleName().split("/")[1]).start(r)).toList();

        for(Thread thread : threads){
            thread.setPriority(priority);
            thread.join();
        }
    }

    public static void runVirtualThreads(List<Runnable> runnables, int priority) throws InterruptedException {
        List<Thread> threads = runnables.stream().map(r -> Thread.ofVirtual().name(r.getClass().getSimpleName().split("/")[1]).start(r)).toList();

        for(Thread thread : threads){
            thread.setPriority(priority);
            thread.join();
        }
    }

    public static List<Runnable> getDBRunnables() {
        Runnable firstRunnable = () -> {
            try {
                FileManagementService.importDatabase("Brazil - 2021");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable secondRunnable = () -> {
            try {
                FileManagementService.importDatabase("Brazil - 2022");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable thirdRunnable = () -> {
            try {
                FileManagementService.importDatabase("Brazil - 2023");
                FileManagementService.importDatabase("Uruguay - 2019");
                FileManagementService.importDatabase("Uruguay - 2020");
                FileManagementService.importDatabase("Uruguay - 2021");
                FileManagementService.importDatabase("Uruguay - 2022");
                FileManagementService.importDatabase("Uruguay - 2023");
                System.gc();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return List.of(firstRunnable, secondRunnable, thirdRunnable);
    }

    public static List<Runnable> getDefaultTasks() throws FileNotFoundException {
        List<Point> locations = FileManagementService.importLocations();

        Runnable firstTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(0, locations.size() / 3));

        Runnable secondTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(locations.size() / 3, locations.size() / 3 * 2));

        Runnable thirdTask = () -> InterpolationService.exportInterpolation(new MomentIterator("01/01/2021", "31/12/2023"), locations.subList(locations.size() / 3 * 2, locations.size()));

        return List.of(firstTask, secondTask, thirdTask);
    }
}
