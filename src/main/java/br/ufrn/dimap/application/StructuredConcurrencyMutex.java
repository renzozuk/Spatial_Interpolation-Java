package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.*;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class StructuredConcurrencyMutex {
    public static void main(String[] args) throws InterruptedException {
        final long checkpoint1 = System.currentTimeMillis();

        runStructuredConcurrency(getMutexVersionOfImportationTasksForThreads());

        final long checkpoint2 = System.currentTimeMillis();

        runStructuredConcurrency(getInterpolationTasks());

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runStructuredConcurrency(getExportationTask());

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
