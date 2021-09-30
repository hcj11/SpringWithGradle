package ThreadPool;

public class Profiler {
    static ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(() -> System.currentTimeMillis());

}
