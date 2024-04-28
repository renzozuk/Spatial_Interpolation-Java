package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getSemaphoreVersionOfExportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.getSemaphoreVersionOfImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runVirtualThreads;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class VirtualThreadsS {
    public static void main(String[] args) throws InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        runVirtualThreads(getSemaphoreVersionOfImportationTasksForThreads());

        long checkpoint2 = System.currentTimeMillis();

        runVirtualThreads(getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runVirtualThreads(getSemaphoreVersionOfExportationTasksForThreads());

        long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
