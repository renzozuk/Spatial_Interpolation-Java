package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getExportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runVirtualThreads;

public class VirtualThreads {
    public static void main(String[] args) throws InterruptedException {
        runVirtualThreads(getImportationTasksForThreads());

        long checkpoint1 = System.currentTimeMillis();

        runVirtualThreads(getInterpolationTasks());

        long checkpoint2 = System.currentTimeMillis();

        runVirtualThreads(getExportationTasksForThreads());

        printResult(checkpoint1, checkpoint2);
    }
}
