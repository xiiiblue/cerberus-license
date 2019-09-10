package cn.telchina.cerberus.machineinfo;

/**
 * 获取硬件信息
 */
public interface MachineInfo {
    /**
     * 获取MAC地址
     *
     * @return MAC地址
     */
    String getMac();

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    String getIp();

    /**
     * 获取CPU型号
     *
     * @return CPU型号
     */
    String getCpu();

    /**
     * 获取主机序列号
     *
     * @return 序列号
     */
    String getSerial();
}
