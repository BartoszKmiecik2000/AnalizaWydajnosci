import java.util.ArrayList;

public class Main {
    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }

        ArrayList<Integer> fib = new ArrayList<>(n + 1);
        fib.add(0);
        fib.add(1);

        for (int i = 2; i <= n; i++) {
            fib.add(fib.get(i - 1) + fib.get(i - 2));
        }

        return fib.get(n);
    }

    public static void main(String[] args) {
        int n = 250;
        long start = System.currentTimeMillis();
        int result = fibonacci(n);
        long stop = System.currentTimeMillis();
        System.out.println("Liczba ciagu Fibonacciego o numerze 30 to: " + result);
        System.out.println("Czas wykonania programu to " + (stop - start) + " ms.");
    }
}