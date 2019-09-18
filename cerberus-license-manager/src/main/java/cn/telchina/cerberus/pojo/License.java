package cn.telchina.cerberus.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 固定格式License
 * 具体的权限信息不强制格式，可以序列化后放到extra字段
 */
@Data
public class License implements Serializable {
    // UUID
    private String licenseId;
    // 主题
    private String subject;
    // 签发时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date issuedDate;
    // 截止时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date expireDate;
    // 授权类型
    private String authType;
    // 主机指纹
    private String fingerprint;
    // 附加信息
    private String extra;
    // 签名
    private String sign;
}
