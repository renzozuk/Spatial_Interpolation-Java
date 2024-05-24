package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasksUsingParallelStreams;
import static br.ufrn.dimap.services.ExecutionService.getMutexVersionOfImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runPlatformThreads;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class PlatformThreadsMutexParallelStream {
    public static void main(String[] args) throws InterruptedException {
        final long checkpoint1 = System.currentTimeMillis();

        runPlatformThreads(getMutexVersionOfImportationTasksForThreads());

        final long checkpoint2 = System.currentTimeMillis();

        runPlatformThreads(getInterpolationTasksUsingParallelStreams());

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runPlatformThreads(getExportationTask());

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
