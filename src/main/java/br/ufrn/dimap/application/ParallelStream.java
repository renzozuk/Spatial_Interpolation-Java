package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getImportationTasksForSerial;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTaskUsingParallelStreams;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runSerial;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class ParallelStream {
    public static void main(String[] args) {
        final long checkpoint1 = System.currentTimeMillis();

        runSerial(getImportationTasksForSerial());

        final long checkpoint2 = System.currentTimeMillis();

        runSerial(getInterpolationTaskUsingParallelStreams());

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        runSerial(getExportationTask());

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
