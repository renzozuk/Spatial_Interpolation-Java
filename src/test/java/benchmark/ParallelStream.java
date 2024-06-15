package benchmark;

import br.ufrn.dimap.services.FileManagementService;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

import static br.ufrn.dimap.services.ExecutionService.getInterpolationTaskUsingParallelStreams;
import static br.ufrn.dimap.services.ExecutionService.runSerial;

@State(Scope.Benchmark)
public class ParallelStream {
    @Setup
    public void loadDataset() throws IOException {
        FileManagementService.importRandomData();
        FileManagementService.importUnknownLocations();
    }

    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @Fork(value = 2)
    public void execute() {
        runSerial(getInterpolationTaskUsingParallelStreams());
    }
}
