package application;

import services.ExecutionService;

public class VirtualThreads {
    public static void main(String[] args) throws InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        ExecutionService.runVirtualThreads(ExecutionService.getImportationTasks());

        long checkpoint2 = System.currentTimeMillis();

        ExecutionService.runVirtualThreads(ExecutionService.getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        ExecutionService.runVirtualThreads(ExecutionService.getExportationTasks());

        long checkpoint4 = System.currentTimeMillis();

        ExecutionService.printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
