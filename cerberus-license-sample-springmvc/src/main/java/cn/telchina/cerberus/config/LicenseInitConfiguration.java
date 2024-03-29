package cn.telchina.cerberus.config;

import cn.telchina.cerberus.exception.LicenseVerifyException;
import cn.telchina.cerberus.license.LicenseVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * License初始化
 * 同时在应用启动时校验License签名/有效期/硬件指纹
 */
@Slf4j
@Configuration
public class LicenseInitConfiguration {
    @Value("${sample.pubkey}")
    private String pubkey;

    @Value("${sample.license}")
    private String license;

    @Bean
    public LicenseVerifier licenseVerifier() {
        LicenseVerifier verifier = new LicenseVerifier().setPublicKey(pubkey).setLicense(license);
        log.info("License初始化完成");

        // 有效性校验
        if (!verifier.checkSign()) {
            throw new LicenseVerifyException("License签名不正确");
        }

        if (!verifier.checkExpire()) {
            throw new LicenseVerifyException("License已超出有效期");
        }

        if (!verifier.checkFingerprint()) {
            throw new LicenseVerifyException("License硬件指纹不匹配");
        }

        // 到期提醒
        LocalDateTime expiredTime = verifier.getLicense().getExpireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime warningTime = LocalDateTime.now().plusMonths(1);
        if (warningTime.isAfter(expiredTime)) {
            log.warn("License即将到期，到期时间为: {}", expiredTime);
        }

        return verifier;
    }
}
