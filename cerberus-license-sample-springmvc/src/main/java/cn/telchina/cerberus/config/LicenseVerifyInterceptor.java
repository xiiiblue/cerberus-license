package cn.telchina.cerberus.config;

import cn.telchina.cerberus.license.LicenseVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * License有效期校验(Interceptor方式)
 */
@Slf4j
@Configuration
class LicenseVerifyInterceptor implements WebMvcConfigurer {
    @Autowired
    private LicenseVerifier verifier;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                boolean check = verifier.checkExpire();
                log.debug("License有效期校验(Interceptor): {}", check);

                if (!check) {
                    log.error("License已超出有效期");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/**");
    }
}