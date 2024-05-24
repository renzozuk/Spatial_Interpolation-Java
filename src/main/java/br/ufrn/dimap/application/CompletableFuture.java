package br.ufrn.dimap.application;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static br.ufrn.dimap.services.ExecutionService.*;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

public class CompletableFuture {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        final long checkpoint1 = System.currentTimeMillis();

        var importationFutures = importThroughCompletableFuture();

        final long checkpoint2 = System.currentTimeMillis();

        var interpolationFutures = interpolateThroughCompletableFuture();

        final long checkpoint3 = System.currentTimeMillis();

        defineExportationPath();
        var exportationFuture = exportThroughSingleThreadAndCompletableFuture();

        final long checkpoint4 = System.currentTimeMillis();

        printResult(checkpoint1, checkpoint2, checkpoint3, checkpoint4);
    }
}
