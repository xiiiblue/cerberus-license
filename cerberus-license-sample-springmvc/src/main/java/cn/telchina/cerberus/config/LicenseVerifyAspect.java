package cn.telchina.cerberus.config;

import cn.telchina.cerberus.exception.LicenseVerifyException;
import cn.telchina.cerberus.license.LicenseVerifier;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * License有效期校验(AOP方式)
 */
@Slf4j
@Component
@Aspect
public class LicenseVerifyAspect {
    @Autowired
    private LicenseVerifier verifier;

    @Pointcut("execution(public * cn.telchina.cerberus.controller..*.*(..))")
    public void licensePointcut() {
    }

    @Before("licensePointcut()")
    public void doBefore(JoinPoint joinPoint) {
        boolean check = verifier.checkExpire();
        if(log.isDebugEnabled()) {
            log.debug("License有效期校验(Aspect): {}", check);
        }

        if (!check) {
            throw new LicenseVerifyException("License已超出有效期");
        }
    }
}
