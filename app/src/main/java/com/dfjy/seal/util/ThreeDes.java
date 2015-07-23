package com.dfjy.seal.util;

/**
 * Project：SealCop
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-23
 * Time: 16:50
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class ThreeDes {

    static String TriDes = "DESede/ECB/NoPadding";
    private static String filePath = "D:/OrientDoc/accessory/1/61/Picture_11_Taste.jpg";
    private static String enfilePath = "D:/OrientDoc/accessory/1/61/en.jpg";
    private static String dfilePath = "D:/OrientDoc/accessory/1/61/d.jpg";

    /**
     * 获得附件的byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] getFileByte(String filePath) {
        byte[] bytes = null;
        try {
            File file = new File(filePath);
            FileInputStream is = new FileInputStream(filePath);
            int size = (int) file.length();
            bytes = new byte[size];
            try {
                if (is.read(bytes) == -1) {
                    System.out.print("read error");
                }
                return bytes;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * priKey
     *
     * @param keyString
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] getPrKey(String keyString)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        key = keyString.getBytes();
        Random rand = new Random(61);
        int iSeed =rand.nextInt(100);
        iSeed = 1 + (int) rand.nextInt(100) % 100;
        //int iSeed=38;
        System.out.println(iSeed);
        int iVar = 0;
        for (int l = 0; l < 24;) {
            iVar = key[l];
            iVar ^= iSeed;
            key[l] = (byte) iVar;
            l += 4;
        }
        return key;
    }

    public static void trides_crypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];
            int len = data.length;

            int h = 8 - len % 8;
            if (h != 0) {
                len = data.length + h;
            }
            byte[] needData = null;
            if (len != 0)
                needData = new byte[len];

            for (int i = len - h; i < len; i++) {

                needData[i] = getHex(h);
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            System.arraycopy(key, 0, k, 0, 24);


            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.ENCRYPT_MODE, ky);
            File file = new File(enfilePath);
            FileOutputStream out = new FileOutputStream(file);
            out.write(c.doFinal(needData));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void trides_decrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];
            int len = data.length;

            byte[] needData = null;
            if (len != 0)
                needData = new byte[len];

            for (int i = 0; i < len; i++) {
                needData[i] = 0x00;
            }
            System.arraycopy(data, 0, needData, 0, data.length);
            System.arraycopy(key, 0, k, 0, 24);
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.DECRYPT_MODE, ky);

            File file = new File(dfilePath);
            FileOutputStream out = new FileOutputStream(file);
            out.write(c.doFinal(needData));
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private static byte getHex(int h) {
        byte c = 0x00;
        switch (h) {
            case 1:
                c = 0x01;
                break;
            case 2:
                c = 0x01;
                break;
            case 3:
                c = 0x01;
                break;
            case 4:
                c = 0x01;
                break;
            case 5:
                c = 0x01;
                break;
            case 6:
                c = 0x01;
                break;
            case 7:
                c = 0x01;
                break;
            case 8:
                c = 0x08;
                break;

        }
        return c;
    }

    public static String byte2hex(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String temp = Integer.toHexString(((int) data[i]) & 0xFF);
            for (int t = temp.length(); t < 2; t++) {
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2,
                        i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String keyString = "HEBYHyzglzx/2013/YZGLXT.";// 密钥
        getPrKey(keyString);
        trides_crypt(getPrKey(keyString), getFileByte(filePath));
        trides_decrypt(getPrKey(keyString), getFileByte(enfilePath));

    }
}