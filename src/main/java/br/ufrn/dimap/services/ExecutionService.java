package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.UnknownPoint;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static br.ufrn.dimap.repositories.LocationRepository.getInstance;
import static java.lang.Runtime.getRuntime;

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

    public static void runPlatformThreadsUsingExecutor(Runnable task) {
        try(var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())){
            executorService.submit(task);
        }
    }

    public static void runPlatformThreadsUsingExecutor(Collection<Runnable> tasks) {
        try(var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())){
            for(Runnable task : tasks){
                executorService.submit(task);
            }
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

    public static void runVirtualThreadsUsingExecutor(Runnable task) {
        try(var executorService = Executors.newVirtualThreadPerTaskExecutor()){
            executorService.submit(task);
        }
    }

    public static void runVirtualThreadsUsingExecutor(Collection<Runnable> tasks) {
        try(var executorService = Executors.newVirtualThreadPerTaskExecutor()){
            for(Runnable task : tasks){
                executorService.submit(task);
            }
        }
    }

    public static Set<Runnable> getImportationTasks() {
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

    public static Set<Future<String>> importThroughSingleThreadAndCallable() {
        try(var executionService = Executors.newSingleThreadExecutor()){
            Set<Future<String>> futures = new HashSet<>();

            futures.add(executionService.submit(() -> {
                try {
                    FileManagementService.importRandomData();

                    return "Known points imported successfully.";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            futures.add(executionService.submit(() -> {
                try {
                    FileManagementService.importUnknownLocations();

                    return "Unknown points imported successfully.";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            return futures;
        }
    }

    public static CompletableFuture<Void> importThroughCompletableFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                FileManagementService.importRandomData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Runnable importUnknownPoints = () -> {
            try {
                FileManagementService.importUnknownLocations();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        CompletableFuture<Void> future2 = future1.thenRun(importUnknownPoints);

        future2.get();

        return future2;
    }

    public static Set<Runnable> getInterpolationTasks() {
        return getInterpolationTasks(Runtime.getRuntime().availableProcessors());
    }

    public static Set<Runnable> getInterpolationTasks(int quantity) {
        List<UnknownPoint> unknownPoints = getInstance().getUnknownPointsAsList();

        Set<Runnable> tasks = new HashSet<>();

        IntStream.range(0, quantity).forEach(i -> tasks.add(() -> InterpolationService.assignTemperatureToUnknownPoints(unknownPoints.subList((int) Math.floor((double) unknownPoints.size() / quantity * i), (int) Math.floor((double) unknownPoints.size() / quantity * (i + 1))))));

        return tasks;
    }

    public static Set<Runnable> getInterpolationTasksUsingParallelStreams() {
        return getInterpolationTasksUsingParallelStreams(Runtime.getRuntime().availableProcessors());
    }

    public static Set<Runnable> getInterpolationTasksUsingParallelStreams(int quantity) {
        List<UnknownPoint> unknownPoints = getInstance().getUnknownPointsAsList();

        Set<Runnable> tasks = new HashSet<>();

        IntStream.range(0, quantity).forEach(i -> tasks.add(() -> InterpolationService.assignTemperatureToUnknownPointsInParallel(unknownPoints.subList((int) Math.floor((double) unknownPoints.size() / quantity * i), (int) Math.floor((double) unknownPoints.size() / quantity * (i + 1))))));

        return tasks;
    }

    public static Set<Runnable> getInterpolationTasksPerUnknownPointsQuantity() {
        return getInstance().getUnknownPoints().stream().map(up -> (Runnable) () -> InterpolationService.assignTemperatureToUnknownPoint(up)).collect(Collectors.toUnmodifiableSet());
    }

    public static Runnable getInterpolationTaskUsingParallelStreams() {
        return () -> {
            InterpolationService.assignTemperatureToUnknownPointsInParallel(getInstance().getUnknownPoints());
        };
    }

    public static Set<Future<UnknownPoint>> interpolateThroughPlatformThreadsAndFuture() {
        try(var executionService = Executors.newFixedThreadPool(getRuntime().availableProcessors())){
            Set<Future<UnknownPoint>> futures = new HashSet<>();

            for(UnknownPoint unknownPoint : getInstance().getUnknownPoints()){
                futures.add(executionService.submit(InterpolationService.getInterpolationCallable(unknownPoint)));
            }

            return futures;
        }
    }

    public static Set<Future<UnknownPoint>> interpolateThroughVirtualThreadsAndFuture() {
        try(var executionService = Executors.newVirtualThreadPerTaskExecutor()){
            Set<Future<UnknownPoint>> futureResult = new HashSet<>();

            for(UnknownPoint unknownPoint : getInstance().getUnknownPoints()){
                futureResult.add(executionService.submit(InterpolationService.getInterpolationCallable(unknownPoint)));
            }

            return futureResult;
        }
    }

    public static void runInterpolationAction() {
        InterpolationAction interpolationAction = new InterpolationAction();
        interpolationAction.compute();
    }

    public static Set<CompletableFuture<UnknownPoint>> interpolateThroughCompletableFuture() {
        Set<CompletableFuture<UnknownPoint>> futures = new HashSet<>();

        getInstance().getUnknownPoints().forEach(up -> {
            futures.add(CompletableFuture.supplyAsync(() -> {
                InterpolationService.assignTemperatureToUnknownPoint(up);

                return up;
            }));
        });

        for(CompletableFuture<UnknownPoint> future : futures){
            future.join();
        }

        return futures;
    }

    public static Runnable getExportationTask() {
        return () -> {
            try {
                FileManagementService.exportInterpolations(getInstance().getUnknownPoints());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Set<Future<UnknownPoint>> exportThroughSingleThreadAndCallable() {
        try(var executionService = Executors.newSingleThreadExecutor()){
            Set<Future<UnknownPoint>> futures = new HashSet<>();

            for(UnknownPoint unknownPoint : getInstance().getUnknownPoints()){
                futures.add(executionService.submit(FileManagementService.getExportationCallable(unknownPoint)));
            }

            return futures;
        }
    }

    public static CompletableFuture<Void> exportThroughSingleThreadAndCompletableFuture() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                FileManagementService.exportInterpolations(getInstance().getUnknownPoints());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        future.join();

        return future;
    }

    public static void printResult(Collection<Future<String>> importationFutures, Collection<Future<UnknownPoint>> interpolationFutures, Collection<Future<UnknownPoint>> exportationFutures) throws ExecutionException, InterruptedException {
        for(Future<String> future : importationFutures){
            System.out.println(future.get());
        }

        for(Future<UnknownPoint> future : interpolationFutures){
            System.out.println(future.get());
        }

        for(Future<UnknownPoint> future : exportationFutures){
            System.out.println(future.get());
        }
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
