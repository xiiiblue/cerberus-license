package cn.telchina.cerberus.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Aspect
@Component
public class WebLogAspect {
    private AtomicLong requestId = new AtomicLong(0);

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    HttpServletRequest request;

    @Pointcut("execution(public * cn.telchina.cerberus.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws JsonProcessingException, NoSuchMethodException {
        this.requestId.incrementAndGet();
        this.startTime.set(System.currentTimeMillis());

        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestUrl = requestUrl + "?" + queryString;
        }
        String payload = objectMapper.writeValueAsString(joinPoint.getArgs());

        // 获取Swagger标注中的操作名称
        ApiOperation apiOperation = getAnnotation(joinPoint.getSignature(), ApiOperation.class);
        String operationName = apiOperation == null ? "" : apiOperation.value();
        request.setAttribute("operationName", operationName);

        // 获取类名与方法名
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();


        // 获取RequestParam中的orderId
        String orderId = "#";
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method sMethod = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = sMethod.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof RequestParam)) continue;
                RequestParam requestParam = (RequestParam) annotation;
                if (!"orderId".equals(requestParam.value())) continue;
                orderId = (String) args[argIndex];
            }
        }
        request.setAttribute("orderId", orderId);

        // 日志
        String requestInfo = String.format("请求信息 序号:%s 方法名:%s.%s URL:%s-%s", requestId, className, methodName, method, requestUrl);
        log.info(requestInfo + " 订单号:{} 状态码:{} 操作:{} 状态:{}", orderId, 0, operationName, "开始");
        log.debug("请求报文 {}", payload);
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws JsonProcessingException {
        String orderId = (String) request.getAttribute("orderId");
        String operationName = (String) request.getAttribute("operationName");

        // 日志
        String responseInfo = String.format("响应信息 序号:%s 耗时:%sms", requestId, System.currentTimeMillis() - startTime.get());
        log.info(responseInfo + " 订单号:{} 状态码:{} 操作:{} 状态:{}", orderId, 0, operationName, "结束");
        log.debug("响应报文 {}", objectMapper.writeValueAsString(ret));
    }

    /**
     * 获取方法上的标注
     *
     * @param signature       衔接点
     * @param annotationClass 类型
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     */
    private <T extends Annotation> T getAnnotation(Signature signature, Class<T> annotationClass) throws NoSuchMethodException {
        CodeSignature func = (CodeSignature) signature;
        Class<?> methodClass = func.getDeclaringType();
        Method method = methodClass.getMethod(func.getName(), func.getParameterTypes());
        return method.getAnnotation(annotationClass);
    }
}