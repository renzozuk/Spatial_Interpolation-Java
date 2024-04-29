package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getMutexVersionOfImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runPlatformThreads;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class PlatformThreadsM {
    public static void main(String[] args) throws InterruptedException {
        long checkpoint1 = System.currentTimeMillis();

        runPlatformThreads(getMutexVersionOfImportationTasksForThreads());

        long checkpoint2 = System.currentTimeMillis();

        runPlatformThreads(getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runPlatformThreads(getExportationTask());

        long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}