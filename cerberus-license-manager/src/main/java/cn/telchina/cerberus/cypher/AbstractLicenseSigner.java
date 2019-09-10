package cn.telchina.cerberus.cypher;

import cn.telchina.cerberus.exception.SignAndVerifyException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * License签名抽象类
 */
@Slf4j
public abstract class AbstractLicenseSigner implements LicenseSigner {
    PrivateKey privateKey;
    PublicKey publicKey;

    /**
     * 获取私钥
     *
     * @return 私钥编码
     */
    @Override
    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(this.privateKey.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @return 公钥编码
     */
    @Override
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }

    /**
     * 设置私钥
     *
     * @param key 私钥编码
     */
    @Override
    public void setPrivateKey(String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            this.privateKey = rsa.generatePrivate(spec);
        } catch (Exception e) {
            log.error("context", e);
            throw new SignAndVerifyException("设置私钥异常");
        }
    }

    /**
     * 设置公钥
     *
     * @param key 公钥编码
     */
    @Override
    public void setPublicKey(String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            this.publicKey = rsa.generatePublic(spec);
        } catch (Exception e) {
            log.error("context", e);
            throw new SignAndVerifyException("设置私钥异常");
        }
    }

    /**
     * 保存私钥至文件
     *
     * @param path 路径
     */
    @Override
    public void savePrivateKey(Path path) {
        try {
            Files.write(path, getPrivateKey().getBytes());
        } catch (IOException e) {
            log.error("context", e);
            throw new SignAndVerifyException("保存私钥异常");
        }
    }

    /**
     * 保存公钥至文件
     *
     * @param path 路径
     */
    @Override
    public void savePublicKey(Path path) {
        try {
            Files.write(path, getPublicKey().getBytes());
        } catch (IOException e) {
            log.error("context", e);
            throw new SignAndVerifyException("保存公钥异常");
        }
    }

    /**
     * 从文件读取私钥
     *
     * @param path 路径
     */
    @Override
    public void loadPrivateKey(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            String key = new String(bytes);
            setPrivateKey(key);
        } catch (IOException e) {
            log.error("context", e);
            throw new SignAndVerifyException("读取私钥异常");
        }
    }

    /**
     * 从文件读取公钥
     *
     * @param path 路径
     */
    @Override
    public void loadPublicKey(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            String key = new String(bytes);
            setPublicKey(key);
        } catch (IOException e) {
            log.error("context", e);
            throw new SignAndVerifyException("读取公钥异常");
        }
    }
}
