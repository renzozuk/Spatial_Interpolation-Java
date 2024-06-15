package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutionException;

import static br.ufrn.dimap.services.ExecutionService.*;

@State(Scope.Benchmark)
public class CompletableFuture {
    @Setup
    public void loadDataset() throws ExecutionException, InterruptedException {
        importThroughCompletableFuture();
    }

    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Fork(value = 2)
    public void execute() {
        interpolateThroughCompletableFuture();
    }
}
