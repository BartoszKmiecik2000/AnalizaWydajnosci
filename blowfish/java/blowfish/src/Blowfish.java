public class Blowfish {
    private int[] P = new int[18];
    private int[][] S = new int[4][256];

    public Blowfish(byte[] key, int keyLength) {
        initialize(key, keyLength);
    }

    private void initialize(byte[] key, int keyLength) {
        int[] PInit = {
                0x243f6a88, 0x85a308d3, 0x13198a2e, 0x03707344,
                0xa4093822, 0x299f31d0, 0x082efa98, 0xec4e6c89,
                0x452821e6, 0x38d01377, 0xbe5466cf, 0x34e90c6c,
                0xc0ac29b7, 0xc97c50dd, 0x3f84d5b5, 0xb5470917,
                0x9216d5d9, 0x8979fb1b
        };

        int[][] SInit = {
                {0xd1310ba6, 0x98dfb5ac, 0x2ffd72db, 0xd01adfb7},
                {0x76dc4190, 0x98d220bc, 0xefa8c1d7, 0xf61e2562},
                {0x76dc4190, 0x98d220bc, 0xefa8c1d7, 0xf61e2562},
                {0x76dc4190, 0x98d220bc, 0xefa8c1d7, 0xf61e2562}
        };

        for (int i = 0; i < PInit.length; ++i) {
            P[i] = PInit[i];
        }

        for (int i = 0; i < SInit.length; ++i) {
            System.arraycopy(SInit[i], 0, S[i], 0, SInit[i].length);
        }

        int keyIndex = 0;
        for (int i = 0; i < P.length; ++i) {
            P[i] ^= (key[keyIndex++] << 24) | (key[keyIndex++] << 16) | (key[keyIndex++] << 8) | key[keyIndex++];
            if (keyIndex >= keyLength) {
                keyIndex = 0;
            }
        }

        int left = 0, right = 0;
        for (int i = 0; i < P.length; i += 2) {
            encryptBlock(left, right);
            P[i] = left;
            P[i + 1] = right;
        }

        for (int i = 0; i < S.length; ++i) {
            for (int j = 0; j < S[i].length; j += 2) {
                encryptBlock(left, right);
                S[i][j] = left;
                S[i][j + 1] = right;
            }
        }
    }

    private int F(int x) {
        int a, b, c, d;
        d = x & 0xFF;
        x >>>= 8;
        c = x & 0xFF;
        x >>>= 8;
        b = x & 0xFF;
        x >>>= 8;
        a = x & 0xFF;

        return (S[0][a] + S[1][b]) ^ S[2][c] + S[3][d];
    }

    private void encryptBlock(int left, int right) {
        for (int i = 0; i < 16; i += 2) {
            left ^= P[i];
            right ^= F(left);
            right ^= P[i + 1];
            left ^= F(right);
        }
        left ^= P[16];
        right ^= P[17];

        swap(left, right);
    }

    private void decryptBlock(int left, int right) {
        for (int i = 16; i > 0; i -= 2) {
            left ^= P[i + 1];
            right ^= F(left);
            right ^= P[i];
            left ^= F(right);
        }
        left ^= P[1];
        right ^= P[0];

        swap(left, right);
    }

    private void swap(int left, int right) {
        int temp = left;
        left = right;
        right = temp;
    }

    public void encrypt(byte[] input, byte[] output, int length) {
        for (int i = 0; i < length; i += 8) {
            if (i + 8 <= length) {
                int left = (input[i] << 24) | ((input[i + 1] & 0xFF) << 16) | ((input[i + 2] & 0xFF) << 8) | (input[i + 3] & 0xFF);
                int right = (input[i + 4] << 24) | ((input[i + 5] & 0xFF) << 16) | ((input[i + 6] & 0xFF) << 8) | (input[i + 7] & 0xFF);

                encryptBlock(left, right);

                output[i] = (byte) (left >> 24);
                output[i + 1] = (byte) ((left >> 16) & 0xFF);
                output[i + 2] = (byte) ((left >> 8) & 0xFF);
                output[i + 3] = (byte) (left & 0xFF);

                output[i + 4] = (byte) (right >> 24);
                output[i + 5] = (byte) ((right >> 16) & 0xFF);
                output[i + 6] = (byte) ((right >> 8) & 0xFF);
                output[i + 7] = (byte) (right & 0xFF);
            } else {
                int remaining = length - i;
                byte[] paddedInput = new byte[8];
                System.arraycopy(input, i, paddedInput, 0, remaining);

                int left = (paddedInput[0] << 24) | ((paddedInput[1] & 0xFF) << 16) | ((paddedInput[2] & 0xFF) << 8) | (paddedInput[3] & 0xFF);
                int right = (paddedInput[4] << 24) | ((paddedInput[5] & 0xFF) << 16) | ((paddedInput[6] & 0xFF) << 8) | (paddedInput[7] & 0xFF);

                encryptBlock(left, right);

                for (int j = 0; j < remaining; ++j) {
                    output[i + j] = (byte) ((left >> (24 - j * 8)) & 0xFF);
                }
            }
        }
    }

    public void decrypt(byte[] input, byte[] output, int length) {
        for (int i = 0; i < length; i += 8) {
            int left = (input[i] << 24) | ((input[i + 1] & 0xFF) << 16) | ((input[i + 2] & 0xFF) << 8) | (input[i + 3] & 0xFF);
            int right = (input[i + 4] << 24) | ((input[i + 5] & 0xFF) << 16) | ((input[i + 6] & 0xFF) << 8) | (input[i + 7] & 0xFF);

            decryptBlock(left, right);

            output[i] = (byte) (left >> 24);
            output[i + 1] = (byte) ((left >> 16) & 0xFF);
            output[i + 2] = (byte) ((left >> 8) & 0xFF);
            output[i + 3] = (byte) (left & 0xFF);

            output[i + 4] = (byte) (right >> 24);
            output[i + 5] = (byte) ((right >> 16) & 0xFF);
            output[i + 6] = (byte) ((right >> 8) & 0xFF);
            output[i + 7] = (byte) (right & 0xFF);
        }
    }

    public static void main(String[] args) {
        byte[] key = "ThisIsASecretKey".getBytes();
        byte[] plaintext = "HelloBlowfish".getBytes();
        byte[] ciphertext = new byte[16];
        byte[] decryptedText = new byte[16];

        Blowfish blowfish = new Blowfish(key, key.length);
        blowfish.encrypt(plaintext, ciphertext, plaintext.length);
        blowfish.decrypt(ciphertext, decryptedText, ciphertext.length);

        System.out.println("Original text: " + new String(plaintext));
        System.out.print("Encrypted text: ");
        for (byte b : ciphertext) {
            System.out.printf("%02x ", b & 0xFF);
        }
        System.out.println();

        System.out.println("Decrypted text: " + new String(decryptedText));
    }
}
