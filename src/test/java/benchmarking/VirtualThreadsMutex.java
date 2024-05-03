package benchmarking;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import static br.ufrn.dimap.services.ExecutionService.getExportationTask;
import static br.ufrn.dimap.services.ExecutionService.getInterpolationTasks;
import static br.ufrn.dimap.services.ExecutionService.getMutexVersionOfImportationTasksForThreads;
import static br.ufrn.dimap.services.ExecutionService.runVirtualThreads;
import static br.ufrn.dimap.services.FileManagementService.defineExportationPath;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@Fork(value = 1, warmups = 0)
public class VirtualThreadsMutex {
    @Benchmark
    public void method1() throws InterruptedException {
        runVirtualThreads(getMutexVersionOfImportationTasksForThreads());

        runVirtualThreads(getInterpolationTasks());

        defineExportationPath();
        runVirtualThreads(getExportationTask());
    }
}
