package br.ufrn.dimap.application;

import br.ufrn.dimap.services.ExecutionService;

public class Serial {
    public static void main(String[] args) {
        long checkpoint1 = System.currentTimeMillis();

        ExecutionService.runSerial(ExecutionService.getImportationTasksForSerial());

        long checkpoint2 = System.currentTimeMillis();

        ExecutionService.runSerial(ExecutionService.getInterpolationTasks());

        long checkpoint3 = System.currentTimeMillis();

        ExecutionService.runSerial(ExecutionService.getExportationTasksForSerial());

        long checkpoint4 = System.currentTimeMillis();

        ExecutionService.printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
