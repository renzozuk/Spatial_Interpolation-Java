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

        System.out.printf("Time to read the known and unknown locations: %.3fs%n", (checkpoint2 - checkpoint1) / 1e3);
        System.out.printf("Interpolation time: %.3fs%n", (checkpoint3 - checkpoint2) / 1e3);
        System.out.printf("Time to export the required locations: %.3fs%n", (checkpoint4 - checkpoint3) / 1e3);
        System.out.printf("Total time: %.3fs%n", (checkpoint4 - checkpoint1) / 1e3);
    }
}
