package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getMutexVersionOfImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runVirtualThreadsUsingExecutor;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class VirtualThreadsMutexE {
    public static void main(String[] args) {
        final long checkpoint1 = System.currentTimeMillis();

        runVirtualThreadsUsingExecutor(getMutexVersionOfImportationTasksForThreads());

        final long checkpoint2 = System.currentTimeMillis();

        runVirtualThreadsUsingExecutor(getInterpolationTasks());

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runVirtualThreadsUsingExecutor(getExportationTask());

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
