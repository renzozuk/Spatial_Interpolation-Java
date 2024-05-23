package benchmark;

import br.ufrn.dimap.services.FileManagementService;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.runPlatformThreads;

@State(Scope.Benchmark)
public class PlatformThreadsMutex {
    @Setup
    public void loadDataset() throws IOException {
        FileManagementService.importRandomData();
        FileManagementService.importUnknownLocations();
    }

    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void execute() throws InterruptedException {
        runPlatformThreads(getInterpolationTasks());
    }
}
