package services;

import entities.UnknownPoint;
import repositories.LocationRepository;

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

    public static List<Runnable> getImportationTasks() {
        Runnable importKnownPoints = () -> {
            try {
                FileManagementService.importRandomData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable importUnknownPoints = () -> {
            try {
                FileManagementService.importUnknownLocations();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return List.of(importKnownPoints, importUnknownPoints);
    }

    public static List<Runnable> getInterpolationTasks() {
        List<UnknownPoint> unknownPoints = LocationRepository.getInstance().getUnknownPoints().stream().toList();

        Runnable firstTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(0, unknownPoints.size() / 3));

        Runnable secondTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(unknownPoints.size() / 3, unknownPoints.size() / 3 * 2));

        Runnable thirdTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(unknownPoints.size() / 3 * 2, unknownPoints.size()));

        return List.of(firstTask, secondTask, thirdTask);
    }

    public static List<Runnable> getExportationTasks() {
        List<UnknownPoint> unknownPoints = LocationRepository.getInstance().getUnknownPoints().stream().toList();

        Runnable firstTask = () -> {
            try {
                FileManagementService.exportInterpolations(unknownPoints.subList(0, unknownPoints.size() / 3));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable secondTask = () -> {
            try {
                FileManagementService.exportInterpolations(unknownPoints.subList(unknownPoints.size() / 3, unknownPoints.size() / 3 * 2));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable thirdTask = () -> {
            try {
                FileManagementService.exportInterpolations(unknownPoints.subList(unknownPoints.size() / 3 * 2, unknownPoints.size()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return List.of(firstTask, secondTask, thirdTask);
    }
}
