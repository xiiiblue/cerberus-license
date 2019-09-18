package cn.telchina.cerberus.config;

import cn.telchina.cerberus.exception.LicenseVerifyException;
import cn.telchina.cerberus.license.LicenseVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * License有效期校验(Filter方式)
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "licenseFilter")
public class LicenseVerifyFilter implements Filter {
    @Autowired
    private LicenseVerifier verifier;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean check = verifier.checkExpire();
        if (log.isDebugEnabled()) {
            log.debug("License有效期校验(Filter): {}", check);
        }

        if (!check) {
            throw new LicenseVerifyException("License已超出有效期");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}