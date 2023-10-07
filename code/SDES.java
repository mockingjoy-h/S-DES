import java.io.UnsupportedEncodingException;

public class SDES {
    // 转换装置设定
    static int K1 = 0;
    static int K2 = 0;
    static int P10[] = new int[] { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    static int P8[] = new int[] { 6, 3, 7, 4, 8, 5, 10, 9 };
    static int P4[] = new int[] { 2, 4, 3, 1 };
    static int IP[] = new int[] { 2, 6, 3, 1, 4, 8, 5, 7 };
    static int IPI[] = new int[] { 4, 1, 3, 5, 7, 2, 8, 6 };
    static int EP[] = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };
    static int S0[][] = {
            { 1, 0, 3, 2 },
            { 3, 2, 1, 0 },
            { 0, 2, 1, 3 },
            { 3, 1, 0, 2 },
    };
    static int S1[][] = {
            { 0, 1, 2, 3 },
            { 2, 3, 1, 0 },
            { 3, 0, 1, 2 },
            { 2, 1, 0, 3 },
    };

    // 根据数组交换
    static int Permute(int num, int p[], int pmax) {
        int result = 0;
        for (int i = 0; i < p.length; i++) {
            result <<= 1;
            result |= (num >> (pmax - p[i])) & 1;
        }
        return result;
    }

    // 生成k1,k2
    static void bkey(String Key) {
        int K = Integer.parseInt(Key, 2);
        K = Permute(K, P10, 10);
        int th = 0, tl = 0;
        th = (K >> 5) & 0x1f;
        tl = K & 0x1f;
        // LS-1 向左循环1位
        th = ((th & 0xf) << 1) | ((th & 0x10) >> 4);
        tl = ((tl & 0xf) << 1) | ((tl & 0x10) >> 4);
        K1 = Permute((th << 5) | tl, P8, 10);
        // LS-2 再次向左循环1位
        th = ((th & 0xf) << 1) | ((th & 0x10) >> 4);
        tl = ((tl & 0xf) << 1) | ((tl & 0x10) >> 4);
        K2 = Permute((th << 5) | tl, P8, 10);
    }

    // f函数
    static int F(int R, int K) {
        int t = Permute(R, EP, 4) ^ K;
        int t0 = (t >> 4) & 0xf;
        int t1 = t & 0xf;
        t0 = S0[((t0 & 0x8) >> 2) | (t0 & 1)][(t0 >> 1) & 0x3];
        t1 = S1[((t1 & 0x8) >> 2) | (t1 & 1)][(t1 >> 1) & 0x3];
        t = Permute((t0 << 2) | t1, P4, 4);
        return t;
    }

    // fk函数
    static int fk(int input, int k) {
        int l = (input >> 4) & 0xf;
        int r = input & 0xf;
        return ((l ^ F(r, k)) << 4) | r;
    }

    // 交换函数
    static int SW(int x) {
        return ((x & 0xf) << 4) | ((x >> 4) & 0xf);
    }

    // 加密
    static String encrypt(String input, String key) {
        bkey(key);
        int m = Integer.parseInt(input, 2);
        m = Permute(m, IP, 8);
        m = fk(m, K1);
        m = SW(m);
        m = fk(m, K2);
        m = Permute(m, IPI, 8);
        return String.format("%08d", Integer.parseInt(Integer.toBinaryString(m)));
    }

    // 解密
    static String decrypt(String input, String key) {
        bkey(key);
        int m = Integer.parseInt(input, 2);
        m = Permute(m, IP, 8);
        m = fk(m, K2);
        m = SW(m);
        m = fk(m, K1);
        m = Permute(m, IPI, 8);
        return String.format("%08d", Integer.parseInt(Integer.toBinaryString(m)));
    }

    // ASCII码加密
    static String encryptASCII(String input, String key) {
        bkey(key);
        try {
            byte[] asciiBytes = input.getBytes("US-ASCII"); // 将 ASCII 字符串转换为字节形式
            StringBuilder ciphertext = new StringBuilder();
            for (byte b : asciiBytes) {
                int m = (int) b; // 以整数形式获取 ASCII 值
                m = Permute(m, IP, 8);
                m = fk(m, K1);
                m = SW(m);
                m = fk(m, K2);
                m = Permute(m, IPI, 8);
                // 将结果整数转换回字节并附加到密文
                ciphertext.append((char) m);
            }
            return ciphertext.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ASCII码解密
    static String decryptASCII(String input, String key) {
        bkey(key);
        try {
            byte[] asciiBytes = input.getBytes("US-ASCII"); // 将 ASCII 字符串转换为字节形式
            StringBuilder plaintext = new StringBuilder();
            for (byte b : asciiBytes) {
                int m = (int) b; // 以整数形式获取 ASCII 值
                m = Permute(m, IP, 8);
                m = fk(m, K2);
                m = SW(m);
                m = fk(m, K1);
                m = Permute(m, IPI, 8);
                //  将结果整数转换回字节并附加到明文
                plaintext.append((char) m);
            }
            return plaintext.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}