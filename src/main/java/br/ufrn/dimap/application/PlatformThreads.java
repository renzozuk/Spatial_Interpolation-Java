package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getExportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runPlatformThreads;

public class PlatformThreads {
    public static void main(String[] args) throws InterruptedException {
        runPlatformThreads(getImportationTasksForThreads());

        long checkpoint1 = System.currentTimeMillis();

        runPlatformThreads(getInterpolationTasks());

        long checkpoint2 = System.currentTimeMillis();

        runPlatformThreads(getExportationTasksForThreads());

        printResult(checkpoint1, checkpoint2);
    }
}
