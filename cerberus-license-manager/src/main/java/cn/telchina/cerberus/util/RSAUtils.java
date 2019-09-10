package cn.telchina.cerberus.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

public class RSAUtils {
    // 加密算法
    private static final String cypherAlgorithm = "RSA";
    // 签名算法
    private static final String signAlgorithm = "MD5withRSA";

    private RSAUtils() {
    }

    /**
     * 创建RSA公私钥对
     *
     * @return 公私钥对
     */
    public static KeyPair generateKey() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(cypherAlgorithm);
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * 通过密码生成RSA公私钥对
     *
     * @param password 密码
     * @return 公私钥对
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException  异常
     */
    public static KeyPair createKey(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(cypherAlgorithm);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        secureRandom.setSeed(password.getBytes());
        keyGen.initialize(2048, secureRandom);
        return keyGen.generateKeyPair();
    }

    /**
     * 加密
     *
     * @param key  公钥
     * @param data 原始数据
     * @return 加密数据
     */
    public static byte[] encrypt(PublicKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance(cypherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param key           私钥
     * @param encryptedData 加密数据
     * @return 原始数据
     */
    public static byte[] decrypt(PrivateKey key, byte[] encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance(cypherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }


    /**
     * 签名
     *
     * @param privateKey 私钥
     * @param data       待签名数据
     * @return 签名
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeyException      异常
     * @throws SignatureException       异常
     */
    public static String sign(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 签名
     *
     * @param privateKey 私钥
     * @param data       待签名数据
     * @return 签名
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeyException      异常
     * @throws SignatureException       异常
     */
    public static String sign(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = data.getBytes();
        return RSAUtils.sign(privateKey, bytes);
    }

    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param data      数据
     * @param sign      签名
     * @return boolean
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeyException      异常
     * @throws SignatureException       异常
     */
    public static boolean verify(PublicKey publicKey, byte[] data, String sign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param data      数据
     * @param sign      签名
     * @return boolean
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeyException      异常
     * @throws SignatureException       异常
     */
    public static boolean verify(PublicKey publicKey, String data, String sign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = data.getBytes();
        return RSAUtils.verify(publicKey, bytes, sign);
    }
}