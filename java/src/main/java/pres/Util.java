package pres;

import java.util.concurrent.Callable;

public class Util {
    public static <T> T timed(String label, Callable<T> f) throws Exception {
        long startTime = System.currentTimeMillis();
        T result = f.call();
        long endTime = System.currentTimeMillis();
        System.out.println(label + " took: " + (endTime - startTime) + " ms");
        return result;
    }
}
