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

    public static long sumUpTo(int max) {
        return ((long) max * (max + 1)) / 2;
    }

    public static int MAX = 10_000_000;
}
