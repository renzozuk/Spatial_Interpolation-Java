package br.ufrn.dimap.application;

import java.util.concurrent.ExecutionException;

import static br.ufrn.dimap.services.ExecutionService.exportThroughSingleThreadAndCallable;
import static br.ufrn.dimap.services.ExecutionService.importThroughSingleThreadAndCallable;
import static br.ufrn.dimap.services.ExecutionService.interpolateThroughPlatformThreadsAndCallable;
import static br.ufrn.dimap.services.ExecutionService.printResult;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class PlatformThreadsCF {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final long checkpoint1 = System.currentTimeMillis();

        var importationFutures = importThroughSingleThreadAndCallable();

        final long checkpoint2 = System.currentTimeMillis();

        var interpolationFutures = interpolateThroughPlatformThreadsAndCallable();

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        var exportationFutures = exportThroughSingleThreadAndCallable();

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
