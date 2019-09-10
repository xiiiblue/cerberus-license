package cn.telchina.cerberus.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AESUtils {

    /**
     * 加解密算法
     */
    private static final String algorithm = "AES";

    /**
     * 私有构造方法
     */
    private AESUtils() {

    }

    /**
     * 生成随机的密钥
     *
     * @return The key generated.
     */
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    /**
     * 通过密码生成密钥
     *
     * @param password 密码
     * @return AES密钥
     */
    public static SecretKey createKey(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = password.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); //只取前128位
        return new SecretKeySpec(key, algorithm);
    }

    /**
     * AES加密
     *
     * @param secretKey 密钥
     * @param data      原始数据
     * @return 加密数据
     */
    public static byte[] encrypt(SecretKey secretKey, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * AES解密
     *
     * @param secretKey 密钥
     * @param encrypted 加密数据
     * @return 原始数据
     */
    public static byte[] decrypt(SecretKey secretKey, byte[] encrypted) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encrypted);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        // 原始数据
        byte[] message = "Hello world!".getBytes();

        // 创建密钥
        SecretKey secretKey = AESUtils.createKey("password");
        System.out.println(new String(secretKey.getEncoded()));

        // 加密
        byte[] encrypted = AESUtils.encrypt(secretKey, message);

        // 解密
        byte[] decrypted = AESUtils.decrypt(secretKey, encrypted);

        System.out.println("原始数据: " + new String(message));
        System.out.println("加密结果: " + new String(encrypted));
        System.out.println("解密结果: " + new String(decrypted));
    }
}