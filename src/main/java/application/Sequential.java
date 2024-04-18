package application;

import services.ExecutionService;

public class Sequential {
    public static void main(String[] args) {
        long checkpoint1 = System.currentTimeMillis();

        ExecutionService.runSequential(ExecutionService.getImportationTasks());

        long checkpoint2 = System.currentTimeMillis();

        ExecutionService.runSequential(ExecutionService.getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        ExecutionService.runSequential(ExecutionService.getExportationTasks());

        long checkpoint4 = System.currentTimeMillis();

        ExecutionService.printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
