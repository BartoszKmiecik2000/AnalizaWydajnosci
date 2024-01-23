import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWEncodingDecoding {

    public static List<Integer> encoding(String s1) {
        System.out.println("Encoding");
        Map<String, Integer> table = new HashMap<>();
        for (int i = 0; i <= 255; i++) {
            String ch = String.valueOf((char) i);
            table.put(ch, i);
        }

        String p = "", c = "";
        p += s1.charAt(0);
        int code = 256;
        List<Integer> outputCode = new ArrayList<>();
        System.out.println("String\tOutput_Code\tAddition");
        for (int i = 0; i < s1.length(); i++) {
            if (i != s1.length() - 1)
                c += s1.charAt(i + 1);
            if (table.containsKey(p + c)) {
                p = p + c;
            } else {
                System.out.println(p + "\t" + table.get(p) + "\t\t" + p + c + "\t" + code);
                outputCode.add(table.get(p));
                table.put(p + c, code);
                code++;
                p = c;
            }
            c = "";
        }
        System.out.println(p + "\t" + table.get(p));
        outputCode.add(table.get(p));
        return outputCode;
    }

    public static void decoding(List<Integer> op) {
        System.out.println("\nDecoding");
        Map<Integer, String> table = new HashMap<>();
        for (int i = 0; i <= 255; i++) {
            String ch = String.valueOf((char) i);
            table.put(i, ch);
        }

        int old = op.get(0), n;
        String s = table.get(old);
        String c = "";
        c += s.charAt(0);
        System.out.print(s);

        int count = 256;
        for (int i = 0; i < op.size() - 1; i++) {
            n = op.get(i + 1);
            if (!table.containsKey(n)) {
                s = table.get(old);
                s = s + c;
            } else {
                s = table.get(n);
            }
            System.out.print(s);
            c = "";
            c += s.charAt(0);
            table.put(count, table.get(old) + c);
            count++;
            old = n;
        }
    }

    public static void main(String[] args) {
        String s = "WYS*WYGWYS*WYSWYSG";
        long start = System.currentTimeMillis();
        List<Integer> outputCode = encoding(s);
        System.out.print("Output Codes are: ");
        for (int i = 0; i < outputCode.size(); i++) {
            System.out.print(outputCode.get(i) + " ");
        }
        System.out.println();
        decoding(outputCode);
        long stop = System.currentTimeMillis();
        System.out.println("\nCzas wykonania programu to " + (stop - start) + " ms.");
    }
}
