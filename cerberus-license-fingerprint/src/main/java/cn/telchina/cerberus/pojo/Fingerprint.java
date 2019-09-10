package cn.telchina.cerberus.pojo;

import lombok.Data;

/**
 * 硬件信息
 */
@Data
public class Fingerprint {
    // MAC地址
    private String mac;

    // IP地址
    private String ip;

    // CPU型号
    private String cpu;

    // 主板序列号
    private String serial;
}
