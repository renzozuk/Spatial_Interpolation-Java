package benchmark;

import br.ufrn.dimap.services.FileManagementService;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.runSerial;

@State(Scope.Benchmark)
public class Serial {
    @Setup
    public void loadDataset() throws IOException {
        FileManagementService.importRandomData();
        FileManagementService.importUnknownLocations();
    }

    @Benchmark
    @Warmup(iterations = 1)
    @Measurement(iterations = 5)
    @Fork(value = 2)
    public void execute() {
        runSerial(getInterpolationTasks());
    }
}
