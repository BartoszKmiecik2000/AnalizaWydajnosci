import java.util.concurrent.TimeUnit;

public class Main {
    static String rle(String str) {
        int n = str.length();
        StringBuilder outcome = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int counter = 1;
            while (i < n - 1 && str.charAt(i) == str.charAt(i + 1)) {
                counter++;
                i++;
            }
            outcome.append(str.charAt(i));
            outcome.append(counter);
        }

        return outcome.toString();
    }

    public static void main(String[] args) {
        String str = "aaaaaaaaaaaaaaaabbbbbbbbbbbbbbccccccccccccccccccccddddddddddddddddeeeeeeeeeeeeeeeeffffffffffffff";

        long start = System.currentTimeMillis();

        String outcome = rle(str);

        long stop = System.currentTimeMillis();

        System.out.println("Dane wejsciowe: " + str);
        System.out.println("Dane wyjsciowe: " + outcome);
        System.out.println("Czas wykonania programu to " + (stop - start) + " ms.");
    }
}
