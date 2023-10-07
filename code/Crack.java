import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Crack {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要破解的明密文对数量：");
        int numPairs = scanner.nextInt();
        scanner.nextLine();

        List<String> plaintextList = new ArrayList<>();
        List<String> ciphertextList = new ArrayList<>();
        
        for (int i = 0; i < numPairs; i++) {
            System.out.println("请输入第" + (i+1) + "对明密文：");
            System.out.print("明文：");
            String plaintextStr = scanner.nextLine();
            plaintextList.add(plaintextStr);

            System.out.print("密文：");
            String ciphertextStr = scanner.nextLine();
            ciphertextList.add(ciphertextStr);
        }
        
        scanner.close();

        long totalDecryptionTime = 0;
        int numberOfPairs = 0; 

        for (int pair = 0; pair < plaintextList.size(); pair++) {
            String plaintext = plaintextList.get(pair);
            String ciphertext = ciphertextList.get(pair);

            long startTime = System.nanoTime();

            for (int i = 0; i < 1024; i++) { 
                String binaryKey = Integer.toBinaryString(i);
                while (binaryKey.length() < 10) {
                    binaryKey = "0" + binaryKey;
                }

                String decrypted = SDES.decrypt(ciphertext, binaryKey);

                if (plaintext.equals(decrypted)) {
                    long endTime = System.nanoTime();
                    long decryptionTime = endTime - startTime;
                    totalDecryptionTime += decryptionTime;
                    numberOfPairs++;
                    System.out.println("找到秘钥" + numberOfPairs + ": " + binaryKey);
                    System.out.println("破解时间: " + decryptionTime / 1000000 + " 毫秒");
                    break;
                }
            }
        }

        if (numberOfPairs > 0) {
            long averageDecryptionTime = totalDecryptionTime / numberOfPairs / 1000000;
            System.out.println("平均破解时间为 " + averageDecryptionTime + " 毫秒");
        } 
        else {
            System.out.println("未找到相应密钥");
        }
    }
}