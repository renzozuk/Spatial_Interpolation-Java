package application;

import services.ExecutionService;

public class PlatformThreads {
    public static void main(String[] args) throws InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        ExecutionService.runPlatformThreads(ExecutionService.getImportationTasks());

        long checkpoint2 = System.currentTimeMillis();

        ExecutionService.runPlatformThreads(ExecutionService.getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        ExecutionService.runPlatformThreads(ExecutionService.getExportationTasks());

        long checkpoint4 = System.currentTimeMillis();

        System.out.printf("Time to read the known and unknown locations: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
        System.out.printf("Interpolation time: %.3fs%n", (checkpoint3 - checkpoint2) / 1e3);
        System.out.printf("Time to export the required locations: %.3fs%n", (checkpoint4 - checkpoint3) / 1e3);
        System.out.printf("Total time: %.3fs%n", (checkpoint4 - checkpoint1) / 1e3);
    }
}
