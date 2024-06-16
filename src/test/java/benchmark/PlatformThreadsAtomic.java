package benchmark;

import br.ufrn.dimap.services.FileManagementService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;

import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.runPlatformThreads;

@State(Scope.Benchmark)
public class PlatformThreadsAtomic {
    @Setup
    public void loadDataset() throws IOException {
        FileManagementService.importRandomData();
        FileManagementService.importUnknownLocations();
    }

    @Benchmark
    @Warmup(iterations = 1)
    @Measurement(iterations = 5)
    @Fork(value = 2)
    public void execute() throws InterruptedException {
        runPlatformThreads(getInterpolationTasks());
    }
}
