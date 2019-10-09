package cn.telchina.cerberus.config;

import cn.telchina.cerberus.license.LicenseVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * License到期提醒(Filter方式)
 * 参考 RFC 7234, section 5.5: Warning
 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Warning
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "licenseExpireWarningFilter")
public class LicenseExpireWariningFilter implements Filter {
    @Autowired
    private LicenseVerifier verifier;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LocalDateTime expiredTime = verifier.getLicense().getExpireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime warningTime = LocalDateTime.now().plusMonths(1);
        if (warningTime.isAfter(expiredTime)) {
            log.warn("License即将到期，到期时间为: {}", expiredTime);

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Warning", "299 cerberus-license/1.0.0 \"License is about to expire: " + expiredTime + "\"");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}