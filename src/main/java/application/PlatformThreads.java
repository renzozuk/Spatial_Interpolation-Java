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

        ExecutionService.printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
