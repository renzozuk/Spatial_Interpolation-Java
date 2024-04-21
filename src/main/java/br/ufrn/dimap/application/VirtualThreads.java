package br.ufrn.dimap.application;

import br.ufrn.dimap.services.ExecutionService;

public class VirtualThreads {
    public static void main(String[] args) throws InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        ExecutionService.runVirtualThreads(ExecutionService.getImportationTasksForThreads());

        long checkpoint2 = System.currentTimeMillis();

        ExecutionService.runVirtualThreads(ExecutionService.getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        ExecutionService.runVirtualThreads(ExecutionService.getExportationTasksForThreads());

        long checkpoint4 = System.currentTimeMillis();

        ExecutionService.printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
