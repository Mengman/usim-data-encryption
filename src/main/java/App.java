import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.print("Please enter transmit key: ");
        String transmitKey = readInput();
        System.out.println(transmitKey);

        try {
            FileKeyDecrypter fileKeyDecrypter = new FileKeyDecrypter(transmitKey);
            System.out.println("Decrypt file key C_2_100_2014082108_FK.txt");
            String fileKey = fileKeyDecrypter.decryptFileKey("data/C_2_100_2014082108_FK.txt");
            System.out.println("Decrypted file key: " + fileKey);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String readInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
