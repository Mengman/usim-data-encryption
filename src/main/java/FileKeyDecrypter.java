import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yucai on 2017/6/19.
 * Email: yucai.li@hpe.com
 */
public class FileKeyDecrypter {
    private String transmitKey;
    private Cipher cipher;
    private SecretKeySpec keySpec;

    public FileKeyDecrypter(String transmitKey) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.transmitKey = transmitKey;
        keySpec = new SecretKeySpec(transmitKey.getBytes(), "DES");
        cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
    }

    public String decryptFileKey(String filePath) {
        File file = new File(filePath);

        try (InputStream inputStream = new FileInputStream(file)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String cipherText = bufferedReader.readLine();
            return decrypt(cipherText.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decrypt(byte[] cipherText) throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final MessageDigest messageDigest = MessageDigest.getInstance("md5");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        final byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }
}
