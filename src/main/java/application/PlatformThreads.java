package application;

import services.ExecutionService;

import java.io.FileNotFoundException;

public class PlatformThreads {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        long checkpoint1 = System.currentTimeMillis();

        ExecutionService.runPlatformThreads(ExecutionService.getDBRunnables());

        long checkpoint2 = System.currentTimeMillis();

        ExecutionService.runPlatformThreads(ExecutionService.getDefaultTasks());

        long checkpoint3 = System.currentTimeMillis();

        System.out.printf("Time to read the database: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
        System.out.printf("Time to export the required locations: %.3fs%n", (checkpoint3 - checkpoint2) / 1e3);
        System.out.printf("Total time: %.3fs%n", (checkpoint3 - checkpoint1) / 1e3);
    }
}
