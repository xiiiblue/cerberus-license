package cn.telchina.cerberus.license;

import cn.telchina.cerberus.cypher.LicenseSigner;
import cn.telchina.cerberus.fingerprint.FingerprintManager;
import cn.telchina.cerberus.pojo.License;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.time.Instant;


/**
 * License校验类
 */
@Slf4j
public class LicenseVerifier extends AbstractLicenseManager {
    private FingerprintManager fingerprintManager;

    public LicenseVerifier() {
        super();
        this.fingerprintManager = new FingerprintManager();
    }

    public LicenseVerifier(LicenseSigner signer, FingerprintManager fingerprintManager) {
        super(signer);
        this.fingerprintManager = fingerprintManager;
    }

    /**
     * 设置公钥
     *
     * @param publicKey 公钥
     * @return this
     */
    public LicenseVerifier setPublicKey(String publicKey) {
        this.signer.setPublicKey(publicKey);
        return this;
    }

    /**
     * 从文件读入公钥
     *
     * @param path 路径
     * @return this
     */
    public LicenseVerifier loadPublicKey(Path path) {
        this.signer.loadPublicKey(path);
        return this;
    }

    /**
     * 设置License
     *
     * @param enc License编码
     * @return this
     */
    public LicenseVerifier setLicense(String enc) {
        this.license = decode(enc);
        return this;
    }

    /**
     * 从文件读入License
     *
     * @param path 路径
     * @return this
     */
    public LicenseVerifier loadLicense(Path path) {
        String enc = readFile(path);
        this.license = decode(enc);

        return this;
    }

    /**
     * 检查License有效性
     *
     * @return boolean
     */
    public boolean checkSign() {
        return this.verify();
    }

    /**
     * 检查License有效期
     *
     * @return boolean
     */
    public boolean checkExpire() {
        return this.getLicense().getExpireDate().toInstant().isAfter(Instant.now());
    }

    /**
     * 检查License主机指纹
     *
     * @return boolean
     */
    public boolean checkFingerprint() {
        return fingerprintManager.verify(this.license.getFingerprint());
    }

    /**
     * 获取License内容
     *
     * @return License实例
     */
    public License getLicense() {
        return this.license;
    }
}
