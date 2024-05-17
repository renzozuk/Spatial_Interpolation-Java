package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasksPerUnknownPointsQuantity;
import static br.ufrn.dimap.services.ExecutionService.getMutexVersionOfImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runPlatformThreadsUsingExecutor;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class PlatformThreadsMutexE {
    public static void main(String[] args) throws InterruptedException {
        final long checkpoint1 = System.currentTimeMillis();

        runPlatformThreadsUsingExecutor(getMutexVersionOfImportationTasksForThreads());

        final long checkpoint2 = System.currentTimeMillis();

        runPlatformThreadsUsingExecutor(getInterpolationTasksPerUnknownPointsQuantity());

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runPlatformThreadsUsingExecutor(getExportationTask());

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
