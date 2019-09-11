package cn.telchina.cerberus.controller;

import cn.telchina.cerberus.dto.LicenseData;
import cn.telchina.cerberus.dto.LicenseRequest;
import cn.telchina.cerberus.license.LicenseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@Api(description = "License管理API")
@RequestMapping("${custom.baseurl}")
public class LicenseController {
    @Value("${keypair.private-key}")
    private String privateKey;

    private LicenseBuilder licenseBuilder;

    @PostConstruct
    public void init() {
        licenseBuilder = new LicenseBuilder().setPrivateKey(privateKey).init();
    }

    @ApiOperation(value = "生成License")
    @PostMapping(value = "/licenses")
    public LicenseData checkLog(@Valid
                                @ApiParam(value = "License请求内容", required = true)
                                @RequestBody LicenseRequest licenseRequest) {
        String license = this.licenseBuilder
                .setSubject(licenseRequest.getSubject())
                .setAuthType(licenseRequest.getType())
                .setExpireDate(licenseRequest.getPeriod())
                .setFingerprint(licenseRequest.getFingerprint())
                .setFingerprint(licenseRequest.getExtra())
                .build();

        return new LicenseData(license);
    }
}