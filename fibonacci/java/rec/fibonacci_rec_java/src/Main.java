public class Main {
    public static void main(String[] args) {
        int n = 40;
        long start = System.currentTimeMillis();
        long result = fibonacci(n);
        long stop = System.currentTimeMillis();
        System.out.println("Liczba ciagu Fibonacciego o numerze 30 to: " + result);
        System.out.println("Czas wykonania programu to " + (stop - start) + " ms.");
    }
    public static long fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}