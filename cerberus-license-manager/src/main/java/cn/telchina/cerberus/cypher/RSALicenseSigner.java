package cn.telchina.cerberus.cypher;

import cn.telchina.cerberus.exception.LicenseVerifyException;
import cn.telchina.cerberus.exception.SignAndVerifyException;
import cn.telchina.cerberus.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * License签名类（RSA算法）
 */
@Slf4j
public class RSALicenseSigner extends AbstractLicenseSigner {
    /**
     * 生成公私钥对
     */
    @Override
    public void generate() {
        KeyPair keyPair;
        try {
            keyPair = RSAUtils.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error("context", e);
            throw new SignAndVerifyException("生成RSA公私钥对异常");
        }
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    /**
     * 签名
     *
     * @param data 被签名数据
     * @return 签名
     */
    @Override
    public String sign(String data) {
        try {
            return RSAUtils.sign(this.privateKey, data);
        } catch (Exception e) {
            log.error("context", e);
            throw new SignAndVerifyException("生成RSA签名异常");
        }
    }

    /**
     * 验签
     *
     * @param data 被签名数据
     * @param sign 签名
     * @return boolean
     */
    @Override
    public boolean verify(String data, String sign) {
        try {
            return RSAUtils.verify(this.publicKey, data, sign);
        } catch (Exception e) {
            log.error("context", e);
            throw new LicenseVerifyException("签名验证失败");
        }
    }
}
