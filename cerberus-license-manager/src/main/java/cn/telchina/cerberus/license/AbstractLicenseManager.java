package cn.telchina.cerberus.license;

import cn.telchina.cerberus.cypher.LicenseSigner;
import cn.telchina.cerberus.cypher.RSALicenseSigner;
import cn.telchina.cerberus.exception.LicenseBuildException;
import cn.telchina.cerberus.exception.LicenseVerifyException;
import cn.telchina.cerberus.pojo.License;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * License管理抽象类
 */
@Slf4j
abstract class AbstractLicenseManager {
    License license;
    LicenseSigner signer;
    private ObjectMapper objectMapper = new ObjectMapper();

    AbstractLicenseManager() {
        this.signer = new RSALicenseSigner();
    }

    AbstractLicenseManager(LicenseSigner signer) {
        this.signer = signer;
    }

    /**
     * License签名
     */
    void sign() {
        String signData = buildSignData();
        String sign = signer.sign(signData);
        this.license.setSign(sign);
    }

    /**
     * License验签
     *
     * @return boolean
     */
    boolean verify() {
        String data = buildSignData();
        String sign = this.license.getSign();
        if (Strings.isNullOrEmpty(sign)) {
            throw new LicenseVerifyException("签名为空");
        }
        return signer.verify(data, sign);
    }

    /**
     * 编码License
     *
     * @return License编码
     */
    String encode(License license) {
        String serialize;
        try {
            serialize = objectMapper.writeValueAsString(license);
            log.debug("serialize: {}", serialize);

        } catch (JsonProcessingException e) {
            log.error("context", e);
            throw new LicenseVerifyException("License编码失败");
        }
        byte[] bytes = serialize.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解码License
     *
     * @return License实例
     */
    License decode(String encode) {
        byte[] bytes = Base64.getDecoder().decode(encode);
        String serialize = new String(bytes);
        try {
            return objectMapper.readValue(serialize, License.class);
        } catch (IOException e) {
            log.error("context", e);
            throw new LicenseVerifyException("License解码失败");
        }
    }


    /**
     * 从文件中读取文本
     *
     * @param path 路径
     * @return 字符串
     */
    String readFile(Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("context", e);
            throw new LicenseVerifyException("文件读取失败");
        }

        return new String(bytes);
    }

    /**
     * 向文件中写入文本
     *
     * @param data 字符串
     * @param path 路径
     */
    void writeFile(String data, Path path) {
        try {
            Files.write(path, data.getBytes());
        } catch (IOException e) {
            log.error("context", e);
            throw new LicenseBuildException("文件保存失败");
        }
    }


    /**
     * 检测License完整性
     *
     * @return boolean
     */
    private boolean checkLicense() {
        return !(Strings.isNullOrEmpty(this.license.getLicenseId())
                || Strings.isNullOrEmpty(this.license.getAuthType())
                || Strings.isNullOrEmpty(this.license.getFingerprint())
                || Strings.isNullOrEmpty(this.license.getSubject())
                || Strings.isNullOrEmpty(this.license.getExtra())
                || this.license.getIssuedDate() != null
                || this.license.getExpireDate() != null);
    }

    /**
     * 构建被签名内容
     *
     * @return 被签名内容
     */
    private String buildSignData() {
        if (checkLicense()) {
            throw new LicenseBuildException("License信息不全");
        }

        Map<String, String> contentMap = new LinkedHashMap<>();
        contentMap.put("licenseId", this.license.getLicenseId());
        contentMap.put("subject", this.license.getSubject());
        contentMap.put("authType", this.license.getAuthType());
        contentMap.put("fingerprint", this.license.getFingerprint());
        contentMap.put("extra", this.license.getExtra());
        contentMap.put("issuedDate", this.license.getIssuedDate().toInstant().toString());
        contentMap.put("expireDate", this.license.getExpireDate().toInstant().toString());

        return contentMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.joining("|"));
    }

}
