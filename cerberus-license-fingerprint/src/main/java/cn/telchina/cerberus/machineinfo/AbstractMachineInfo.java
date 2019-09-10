package cn.telchina.cerberus.machineinfo;


import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 硬件信息抽象类
 */
@Slf4j
public abstract class AbstractMachineInfo implements MachineInfo {
    private List<String> networkInterfaces = new LinkedList<>();
    private List<String> inetAddresses = new LinkedList<>();

    AbstractMachineInfo() {
        this.initList();
    }


    /**
     * 获取MAC地址
     *
     * @return MAC地址
     */
    @Override
    public String getMac() {
        return this.networkInterfaces.stream()
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .collect(Collectors.joining("|"));
    }

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    @Override
    public String getIp() {
        return this.inetAddresses.stream()
                .distinct()
                .sorted()
                .collect(Collectors.joining("|"));
    }

    /**
     * 获取CPU型号
     *
     * @return CPU型号
     */
    @Override
    public abstract String getCpu();

    /**
     * 获取主机序列号
     *
     * @return 序列号
     */
    @Override
    public abstract String getSerial();


    /**
     * 获取网卡及IP列表
     */
    private void initList() {
        try {
            // 遍历网卡
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) networkInterfaces.nextElement();

                if (!iface.isLoopback() && !iface.isVirtual() && !iface.isPointToPoint()) {
                    byte[] macHex = iface.getHardwareAddress();
                    if (macHex != null) {
                        this.networkInterfaces.add(this.hexToString(macHex));
                    }

                    // 遍历接口的IP
                    Enumeration inetAddresses = iface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddr = (InetAddress) inetAddresses.nextElement();

                        if (!inetAddr.isLoopbackAddress() && !inetAddr.isLinkLocalAddress() && !inetAddr.isMulticastAddress()) {
                            this.inetAddresses.add(inetAddr.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("context", e);
        }
    }

    /**
     * MAC地址十六进制转字符串
     *
     * @param mac 十六进制MAC地址
     * @return 字符串
     */
    private String hexToString(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append(":");
            }
            String temp = Integer.toHexString(mac[i] & 0xff);
            if (temp.length() == 1) {
                sb.append("0").append(temp);
            } else {
                sb.append(temp);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 运行命令行
     *
     * @param shell 命令
     * @return 结果
     */
    String runCommandLine(String[] shell) {
        String serialNumber = "UNKNOWN";
        try {
            Process process = Runtime.getRuntime().exec(shell);
            process.getOutputStream().close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = reader.readLine().trim();
            if (!Strings.isNullOrEmpty(line)) {
                serialNumber = line;
            }

            reader.close();
        } catch (Exception ex) {
            log.error("content", ex);
        }

        return serialNumber;
    }
}
