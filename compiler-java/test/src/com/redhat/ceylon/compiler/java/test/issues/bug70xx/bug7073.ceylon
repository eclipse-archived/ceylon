import java.util.concurrent { Executors }
import java.lang { Runnable, Thread }

class Test() {
    Thread? threadFactory1(Runnable runnable) => null;
    Executors.newSingleThreadExecutor(threadFactory1);
    
    function threadFactory2(Runnable runnable) => null;
    Executors.newSingleThreadExecutor(threadFactory2);
    
    Executors.newSingleThreadExecutor((r) => null);
}
