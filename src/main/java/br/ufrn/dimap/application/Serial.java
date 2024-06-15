package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getImportationTasks;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runSerial;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class Serial {
    public static void main(String[] args) {
        final long checkpoint1 = System.currentTimeMillis();

        runSerial(getImportationTasks());

        final long checkpoint2 = System.currentTimeMillis();

        runSerial(getInterpolationTasks());

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runSerial(getExportationTask());

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}