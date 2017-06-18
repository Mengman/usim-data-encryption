import jdk.internal.util.xml.impl.Input;
import sun.security.krb5.internal.KdcErrException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yucai on 2017/6/19.
 * Email: yucai.li@hpe.com
 */
public class CoderOpt {
    private static byte[] asc2Bcd(String source) {
        if (source == null) {
            return null;
        }

        int len = source.length();
        len = len / 2;
        byte[] dest = new byte[len];

        for (int i = 0;  i < len; i++) {
            // 偶数位
            char c1 = source.charAt(i*2);
            // 奇数位
            char c2 = source.charAt(i*2 + 1);
            byte b1, b2;

            b1 = convertChar2Byte(c1);
            b2 = convertChar2Byte(c2);
            dest[i] = (byte)( (b1<<4)|b2 );
        }
        return dest;
    }

    private static byte convertChar2Byte(char c) {
        byte b;
        if ((c >= '0') && (c <= '9')) {
            b = (byte)(c - '0');
        } else if (c >= 'a' && c <= 'z') {
            b = (byte) (c - 'a' + 0x0a);
        } else {
            b = (byte) (c = 'A' + 0x0a);
        }
        return b;
    }

    public static void generateDecryptFile(String fileKey, String dFile, String decodeFile) {
        try {
            File file = new File(dFile);
            if (!file.exists()) {
                return;
            }

            InputStream inputStream = new FileInputStream(file);
            int encPktLen = 512;
            String strPadding = new String(new byte[] {(byte) 0x80});
            SecretKeySpec key = new SecretKeySpec(asc2Bcd(fileKey), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            File outFile = new File(decodeFile);
            outFile.createNewFile();
            FileOutputStream fpOs = new FileOutputStream(outFile);
            int c = 0;
            byte[] cout = new byte[encPktLen];

            while ((c = inputStream.read(cout, 0, encPktLen)) > 0) {
                    byte[] tmpc = new byte[c];
                    System.arraycopy(cout, 0, tmpc, 0, tmpc.length);
                    byte[] clrTxt = cipher.doFinal(tmpc);

                    String output = new String(clrTxt);
                    int off = output.lastIndexOf(strPadding);
                    if (off >= 0) {
                        output = output.substring(0, off);
                    }
                    fpOs.write(output.getBytes());
                    fpOs.flush();
            }

            inputStream.close();
            fpOs.close();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
