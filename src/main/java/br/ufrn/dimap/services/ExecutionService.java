package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExecutionService {
    public static void runSerial(Runnable task) {
        task.run();
    }

    public static void runSerial(Collection<Runnable> tasks) {
        tasks.forEach(Runnable::run);
    }

    public static void runPlatformThreads(Runnable task) throws InterruptedException {
        Thread uniqueThread = Thread.ofPlatform().name(task.getClass().getSimpleName().split("/")[1]).start(task);
        uniqueThread.join();
    }

    public static void runPlatformThreads(Collection<Runnable> tasks) throws InterruptedException {
        runPlatformThreads(tasks, Thread.MIN_PRIORITY);
    }

    public static void runPlatformThreads(Collection<Runnable> tasks, int priority) throws InterruptedException {
        for(Thread thread : tasks.stream().map(r -> Thread.ofPlatform().name(r.getClass().getSimpleName().split("/")[1]).start(r)).collect(Collectors.toUnmodifiableSet())){
            thread.setPriority(priority);
            thread.join();
        }
    }

    public static void runVirtualThreads(Runnable task) throws InterruptedException {
        Thread uniqueThread = Thread.ofVirtual().name(task.getClass().getSimpleName().split("/")[1]).start(task);
        uniqueThread.join();
    }

    public static void runVirtualThreads(Collection<Runnable> tasks) throws InterruptedException {
        runVirtualThreads(tasks, Thread.MIN_PRIORITY);
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
            } catch (IOException e) {
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

    public static Set<Runnable> getAtomicVersionOfImportationTasksForThreads() {
        Runnable importKnownPoints = () -> {
            try {
                FileManagementService.importRandomData(LocationRepository.getAtomicInstance());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable importUnknownPoints = () -> {
            try {
                FileManagementService.importUnknownLocations(LocationRepository.getAtomicInstance());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return Set.of(importKnownPoints, importUnknownPoints);
    }

    public static Set<Runnable> getInterpolationTasks() {
        return getInterpolationTasks(Runtime.getRuntime().availableProcessors());
    }

    public static Set<Runnable> getInterpolationTasks(int quantity) {
        List<UnknownPoint> unknownPoints = LocationRepository.getInstance().getUnknownPointsAsAList();

        Set<Runnable> tasks = new HashSet<>();

        IntStream.range(0, quantity).forEach(i -> tasks.add(() -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList((int) Math.floor((double) unknownPoints.size() / quantity * i), (int) Math.floor((double) unknownPoints.size() / quantity * (i + 1))))));

        return tasks;
    }

    public static Runnable getExportationTask() {
        return () -> {
            try {
                FileManagementService.exportInterpolations(LocationRepository.getInstance().getUnknownPoints());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
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
