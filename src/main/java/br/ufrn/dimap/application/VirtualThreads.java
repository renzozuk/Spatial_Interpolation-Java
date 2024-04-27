package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getExportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runVirtualThreads;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class VirtualThreads {
    public static void main(String[] args) throws InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        runVirtualThreads(getImportationTasksForThreads());

        long checkpoint2 = System.currentTimeMillis();

        runVirtualThreads(getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();

        runVirtualThreads(getExportationTasksForThreads());

        long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}