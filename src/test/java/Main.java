import benchmarking.VirtualThreadsMutex;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(VirtualThreadsMutex.class.getSimpleName())
                .warmupIterations(0)
                .shouldDoGC(true)
                .measurementIterations(2).forks(1)
                .jvmArgs("-Xms1g", "-Xmx5g").build();

        new Runner(opt).run();
    }
}
