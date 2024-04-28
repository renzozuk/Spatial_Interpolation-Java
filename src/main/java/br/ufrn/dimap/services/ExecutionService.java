package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ExecutionService {
    public static void runSerial(Collection<Runnable> tasks) {
        tasks.forEach(Runnable::run);
    }

    public static void runPlatformThreads(Collection<Runnable> tasks) throws InterruptedException {
        runPlatformThreads(tasks, Thread.MIN_PRIORITY);
    }

    public static void runVirtualThreads(Collection<Runnable> tasks) throws InterruptedException {
        runVirtualThreads(tasks, Thread.MIN_PRIORITY);
    }

    public static void runPlatformThreads(Collection<Runnable> tasks, int priority) throws InterruptedException {
        for(Thread thread : tasks.stream().map(r -> Thread.ofPlatform().name(r.getClass().getSimpleName().split("/")[1]).start(r)).collect(Collectors.toUnmodifiableSet())){
            thread.setPriority(priority);
            thread.join();
        }
    }

    public static void runVirtualThreads(Collection<Runnable> tasks, int priority) throws InterruptedException {
        for(Thread thread : tasks.stream().map(r -> Thread.ofVirtual().name(r.getClass().getSimpleName().split("/")[1]).start(r)).collect(Collectors.toUnmodifiableSet())){
            thread.setPriority(priority);
            thread.join();
        }
    }

    public static Set<Runnable> getImportationTasksForSerial() {
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

        return Set.of(importKnownPoints, importUnknownPoints);
    }

    public static Set<Runnable> getMutexVersionOfImportationTasksForThreads() {
        Lock lock = new ReentrantLock();

        Runnable importKnownPoints = () -> {
            try {
                FileManagementService.importRandomData(lock);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable importUnknownPoints = () -> {
            try {
                FileManagementService.importUnknownLocations(lock);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return Set.of(importKnownPoints, importUnknownPoints);
    }

    public static Set<Runnable> getSemaphoreVersionOfImportationTasksForThreads() {
        Semaphore semaphore = new Semaphore(1);

        Runnable importKnownPoints = () -> {
            try {
                FileManagementService.importRandomData(semaphore);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable importUnknownPoints = () -> {
            try {
                FileManagementService.importUnknownLocations(semaphore);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        return Set.of(importKnownPoints, importUnknownPoints);
    }

    public static Set<Runnable> getInterpolationTasks() {
        List<UnknownPoint> unknownPoints = LocationRepository.getInstance().getUnknownPoints().stream().toList();

        Runnable firstTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(0, unknownPoints.size() / 4));

        Runnable secondTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(unknownPoints.size() / 4, unknownPoints.size() / 2));

        Runnable thirdTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(unknownPoints.size() / 2, unknownPoints.size() / 4 * 3));

        Runnable fourthTask = () -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList(unknownPoints.size() / 4 * 3, unknownPoints.size()));

        return Set.of(firstTask, secondTask, thirdTask, fourthTask);
    }

    public static Set<Runnable> getExportationTasksForSerial() {
        Runnable firstTask = () -> {
            try {
                FileManagementService.exportInterpolations(LocationRepository.getInstance().getUnknownPoints());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        return Set.of(firstTask);
    }

    public static Set<Runnable> getMutexVersionOfExportationTasksForThreads() {
        Lock lock = new ReentrantLock();

        List<UnknownPoint> unknownPoints = LocationRepository.getInstance().getUnknownPoints().stream().toList();

        Runnable firstTask = () -> {
            try {
                FileManagementService.exportInterpolations(lock, unknownPoints.subList(0, unknownPoints.size() / 3));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable secondTask = () -> {
            try {
                FileManagementService.exportInterpolations(lock, unknownPoints.subList(unknownPoints.size() / 3, unknownPoints.size() / 3 * 2));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable thirdTask = () -> {
            try {
                FileManagementService.exportInterpolations(lock, unknownPoints.subList(unknownPoints.size() / 3 * 2, unknownPoints.size()));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        return Set.of(firstTask, secondTask, thirdTask);
    }

    public static Set<Runnable> getSemaphoreVersionOfExportationTasksForThreads() {
        Semaphore semaphore = new Semaphore(1);

        List<UnknownPoint> unknownPoints = LocationRepository.getInstance().getUnknownPoints().stream().toList();

        Runnable firstTask = () -> {
            try {
                FileManagementService.exportInterpolations(semaphore, unknownPoints.subList(0, unknownPoints.size() / 3));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable secondTask = () -> {
            try {
                FileManagementService.exportInterpolations(semaphore, unknownPoints.subList(unknownPoints.size() / 3, unknownPoints.size() / 3 * 2));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable thirdTask = () -> {
            try {
                FileManagementService.exportInterpolations(semaphore, unknownPoints.subList(unknownPoints.size() / 3 * 2, unknownPoints.size()));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        return Set.of(firstTask, secondTask, thirdTask);
    }

    public static void printResult(long checkpoint1, long checkpoint2) {
        System.out.printf("Interpolation time: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
    }

    public static void printResult(long checkpoint1, long checkpoint2, long checkpoint3, long checkpoint4) {
        System.out.printf("Time to read the known and unknown locations: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
        printResult(checkpoint2, checkpoint3);
        System.out.printf("Time to export the required locations: %.3fs%n", (checkpoint4 - checkpoint3) / 1e3);
        System.out.printf("Total time: %.3fs%n", (checkpoint4 - checkpoint1) / 1e3);
    }
}
