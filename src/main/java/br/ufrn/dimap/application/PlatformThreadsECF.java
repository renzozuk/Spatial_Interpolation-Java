package br.ufrn.dimap.application;

import java.util.concurrent.ExecutionException;

import static br.ufrn.dimap.services.ExecutionService.*;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class PlatformThreadsECF {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final long checkpoint1 = System.currentTimeMillis();

        final var importationFutures = importThroughPlatformThreadsAndCallable();

        final long checkpoint2 = System.currentTimeMillis();

        final var interpolationFutures = interpolateThroughPlatformThreadsAndCallable();

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        final var exportationFutures = exportThroughPlatformThreadsAndCallable();

        final long checkpoint4 = System.currentTimeMillis();

//        printResult(importationFutures, interpolationFutures, exportationFutures);
        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
