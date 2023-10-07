import java.util.Scanner;

public class ClosedTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入明文：");
        String plaintext = scanner.nextLine();
        System.out.print("请输入密文：");
        String ciphertext = scanner.nextLine();
        scanner.close();

        int num = 0;
        for (int i = 0; i < 1024; i++) { 
            String binaryKey = Integer.toBinaryString(i);
            while (binaryKey.length() < 10) {
                binaryKey = "0" + binaryKey;
            }
            String decrypted = SDES.decrypt(ciphertext, binaryKey);
            if (plaintext.equals(decrypted)) {
                num++;
                System.out.println("找到第" + num + "个秘钥" + ": " + binaryKey);
            }
        }
    }
}
