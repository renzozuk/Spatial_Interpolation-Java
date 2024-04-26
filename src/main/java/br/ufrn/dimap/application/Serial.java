package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.getImportationTasksForSerial;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getExportationTasksForSerial;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.ExecutionService.runSerial;

public class Serial {
    public static void main(String[] args) {
        runSerial(getImportationTasksForSerial());

        long checkpoint1 = System.currentTimeMillis();

        runSerial(getInterpolationTasks());

        long checkpoint2 = System.currentTimeMillis();

        runSerial(getExportationTasksForSerial());

        printResult(checkpoint1, checkpoint2);
    }
}
