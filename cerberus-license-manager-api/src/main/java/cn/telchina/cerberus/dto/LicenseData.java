package cn.telchina.cerberus.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api("License内容")
@Data
public class LicenseData {
    @ApiModelProperty(value = "License编码后内容", example = "ab512d15bcd43c1adfcc2f8fb2694c86d397c7b87299ca4230a300745fb007ef")
    private String license;

    public LicenseData(String license) {
        this.license = license;
    }
}