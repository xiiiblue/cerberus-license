package cn.telchina.cerberus.license;

import cn.telchina.cerberus.cypher.LicenseSigner;
import cn.telchina.cerberus.pojo.License;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * License构造类
 */
@Slf4j
public class LicenseBuilder extends AbstractLicenseManager {
    public LicenseBuilder() {
    }

    public LicenseBuilder(LicenseSigner signer) {
        super(signer);
    }

    /**
     * 设置私钥
     *
     * @param privateKey 私钥
     * @return this
     */
    public LicenseBuilder setPrivateKey(String privateKey) {
        this.signer.setPrivateKey(privateKey);
        return this;
    }

    /**
     * 从文件读取私钥
     *
     * @param path 路径
     * @return this
     */
    public LicenseBuilder loadPrivateKey(Path path) {
        this.signer.loadPrivateKey(path);
        return this;
    }


    /**
     * 初始化License
     *
     * @return this
     */
    public LicenseBuilder init() {
        this.license = new License();
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        this.license.setLicenseId(uuid);
        this.license.setIssuedDate(new Date());
        this.setExpireDate("2099-12-31 23:59:59");
        this.setAuthType("BASIC");
        this.setExtra("");
        return this;
    }

    /**
     * 设置License主题
     *
     * @param subject 主题
     * @return this
     */
    public LicenseBuilder setSubject(String subject) {
        this.license.setSubject(subject);
        return this;
    }

    /**
     * 设置License类型
     *
     * @param authType 类型
     * @return this
     */
    public LicenseBuilder setAuthType(String authType) {
        this.license.setAuthType(authType);
        return this;
    }

    /**
     * 设置License机器指纹
     *
     * @param fingerprint 机器指纹
     * @return this
     */
    public LicenseBuilder setFingerprint(String fingerprint) {
        this.license.setFingerprint(fingerprint);
        return this;
    }


    /**
     * 设置License附加信息
     *
     * @param extra 附加信息
     * @return this
     */
    public LicenseBuilder setExtra(String extra) {
        this.license.setExtra(extra);
        return this;
    }

    /**
     * 设置License过期时间
     *
     * @param expireDate 过期时间
     * @return this
     */
    public LicenseBuilder setExpireDate(Date expireDate) {
        this.license.setExpireDate(expireDate);
        return this;
    }

    /**
     * 设置License过期时间
     *
     * @param expireLDT 过期时间
     * @return this
     */
    public LicenseBuilder setExpireDate(LocalDateTime expireLDT) {
        Date expireDate = Date.from(expireLDT.atZone(ZoneId.systemDefault()).toInstant());
        setExpireDate(expireDate);
        return this;
    }

    /**
     * 设置License过期时间
     *
     * @param expireDateString 过期时间字符串（yyyy-MM-dd HH:mm:ss）
     * @return this
     */
    public LicenseBuilder setExpireDate(String expireDateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expireLDT = LocalDateTime.parse(expireDateString, dateTimeFormatter);
        setExpireDate(expireLDT);
        return this;
    }

    /**
     * 设置License过期时间
     *
     * @param monthDuration 有效月份
     * @return this
     */
    public LicenseBuilder setExpireDate(Integer monthDuration) {
        LocalDateTime expireLDT = LocalDateTime.now().plusMonths(monthDuration);
        setExpireDate(expireLDT);
        return this;
    }

    /**
     * 构建License
     *
     * @return License编码
     */
    public String build() {
        sign();
        return encode(this.license);
    }

    /**
     * 导出License至文件
     *
     * @param path 路径
     */
    public void export(Path path) {
        sign();
        writeFile(build(), path);
    }

    /**
     * For jUnit only
     *
     * @param sign 签名
     * @return this
     */
    LicenseBuilder setSign(String sign) {
        this.license.setSign(sign);
        return this;
    }

    /**
     * For jUnit only
     *
     * @return License编码
     */
    String buildWithoutSign() {
        return encode(this.license);
    }
}