package cn.telchina.cerberus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api("错误定义")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ApiError {
    @ApiModelProperty(value = "HTTP状态码(冗余)", example = "999")
    private Integer status;

    @ApiModelProperty(value = "自定义异常代码", example = "999")
    private Integer code;

    @ApiModelProperty(value = "报错信息", example = "异常简单描述")
    private String message;

    @ApiModelProperty(value = "开发者信息", example = "异常详细原因定位")
    private String developerMessage;

    public ApiError(Integer status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ApiError(Integer status, Integer code, String message, String developerMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
    }
}


