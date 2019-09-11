package cn.telchina.cerberus.handler;

import cn.telchina.cerberus.dto.ApiError;
import cn.telchina.cerberus.exception.AlreadyExistsException;
import cn.telchina.cerberus.exception.BusinessException;
import cn.telchina.cerberus.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.net.SocketTimeoutException;
import java.util.Iterator;

/**
 * 公共异常处理
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @Autowired
    HttpServletRequest request;

    private void formattedLog(int code, String message) {
        String orderId = (String) request.getAttribute("orderId");
        String operationName = (String) request.getAttribute("operationName");
        log.error("响应信息 发生异常 订单号:{} 状态码:{} 操作:{} 状态:{}", orderId, code, operationName, message);
    }

    /**
     * ResourceNotFoundException 找不到资源 404 404
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ApiError handleRunTimeException(ResourceNotFoundException ex) {
        this.formattedLog(404, ex.getMessage());
        return new ApiError(404, 404, ex.getMessage());
    }

    /**
     * AlreadyExistsException 资源已存在 409 409
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseBody
    public ApiError handleRunTimeException(AlreadyExistsException ex) {
        this.formattedLog(409, ex.getMessage());
        return new ApiError(409, 409, ex.getMessage());
    }

    /**
     * HttpMessageNotReadableException 请求参数无法识别 409 802
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ApiError handleRunTimeException(HttpMessageNotReadableException ex) {
        this.formattedLog(802, ex.getMessage());
        return new ApiError(409, 802, "请求参数无法识别", ex.getMessage());
    }


    /**
     * BusinessException 通用业务逻辑异常 409 ???
     * 无明确分类的业务逻辑异常，status=406 code=自定义
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiError handleRunTimeException(BusinessException ex) {
        this.formattedLog(ex.getCode(), ex.getMessage());
        return new ApiError(409, ex.getCode(), ex.getMessage());
    }


    /**
     * MethodArgumentTypeMismatchException JSR303参数校验异常 422 422
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ApiError handleRunTimeException(MethodArgumentTypeMismatchException ex) {
        this.formattedLog(422, "参数校验异常:" + ex.getMessage());
        return new ApiError(422, 422, "参数校验异常:" + ex.getMessage());
    }

    /**
     * ConstraintViolationException JSR303参数校验异常 422 422
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ApiError handleRunTimeException(ConstraintViolationException ex) {
        this.formattedLog(422, "参数校验异常:" + ex.getMessage());
        return new ApiError(422, 422, "参数校验异常:" + ex.getMessage());
    }

    /**
     * MissingServletRequestParameterException JSR303参数校验异常 422 422
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ApiError handleRunTimeException(MissingServletRequestParameterException ex) {
        this.formattedLog(422, "参数校验异常:" + ex.getMessage());
        return new ApiError(422, 422, "参数校验异常:" + ex.getMessage());
    }

    /**
     * MethodArgumentNotValidException JSR303参数校验异常 422 422
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiError handleRunTimeException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder("参数验证失败,数量:").append(ex.getBindingResult().getErrorCount());

        Iterator iterator = ex.getBindingResult().getAllErrors().iterator();
        while (iterator.hasNext()) {
            ObjectError error = (ObjectError) iterator.next();
            DefaultMessageSourceResolvable argument = (DefaultMessageSourceResolvable) error.getArguments()[0];
            String errorParameter = argument.getDefaultMessage();
            String errorMessage = error.getDefaultMessage();
            sb.append(" [名称:").append(errorParameter).append(" 描述:").append(errorMessage).append("] ");
        }
        this.formattedLog(422, "参数校验异常:" + sb.toString());
        return new ApiError(422, 422, sb.toString());
    }

    /**
     * SocketTimeoutException 请求超时异常 408 408
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseBody
    public ApiError handleSocketTimeoutException(SocketTimeoutException ex) {
        log.error("context", ex);
        this.formattedLog(408, ex.getMessage());
        return new ApiError(408, 408, "请求超时！");
    }

    /**
     * ResourceAccessException 请求超时异常 408 409
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(ResourceAccessException.class)
    @ResponseBody
    public ApiError handleResourceAccessException(ResourceAccessException ex) {
        log.error("context", ex);
        this.formattedLog(409, ex.getMessage());
        return new ApiError(408, 409, "资源访问异常！");
    }

    /**
     * http连接异常 400
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ApiError handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error("context", ex);
        this.formattedLog(400, ex.getMessage());
        return new ApiError(400, 400, "连接异常！");
    }

    /**
     * Exception 其它异常 500 500
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiError handleRunTimeException(Exception ex) {
        log.error("context", ex);
        this.formattedLog(500, ex.getMessage());
        return new ApiError(500, 500, ex.getMessage());
    }
}