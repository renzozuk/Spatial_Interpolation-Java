package br.ufrn.dimap.application;

import static br.ufrn.dimap.services.ExecutionService.exportThroughSingleThreadAndCallable;
import static br.ufrn.dimap.services.ExecutionService.importThroughSingleThreadAndCallable;
import static br.ufrn.dimap.services.ExecutionService.interpolateThroughVirtualThreadsAndCallable;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class VirtualThreadsCF {
    public static void main(String[] args) {
        final long checkpoint1 = System.currentTimeMillis();

        var importationFutures = importThroughSingleThreadAndCallable();

        final long checkpoint2 = System.currentTimeMillis();

        var interpolationFutures = interpolateThroughVirtualThreadsAndCallable();

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        var exportationFutures = exportThroughSingleThreadAndCallable();

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
