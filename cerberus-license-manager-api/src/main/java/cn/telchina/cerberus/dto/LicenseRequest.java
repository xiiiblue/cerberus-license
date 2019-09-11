package cn.telchina.cerberus.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Api("License请求内容")
@Data
public class LicenseRequest {
    @NotEmpty
    @ApiModelProperty(value = "License主题", example = "YourProject")
    private String subject;

    @NotEmpty
    @ApiModelProperty(value = "授权类型", example = "BASIC")
    private String type;

    @NotNull
    @ApiModelProperty(value = "有效时长(月)", example = "24")
    private Integer period;

    @NotEmpty
    @ApiModelProperty(value = "主机指纹", example = "84ba4cb7040a6b6c688e4ba84884c2fc8d022e2d7741192cea353191f6ae6835")
    private String fingerprint;

    @ApiModelProperty(value = "附加信息", example = "foobar")
    private String extra;

    public LicenseRequest(String subject, String type, Integer period, String fingerprint, String extra) {
        this.subject = subject;
        this.type = type;
        this.period = period;
        this.fingerprint = fingerprint;
        this.extra = extra;
    }
}