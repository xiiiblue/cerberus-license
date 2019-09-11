package cn.telchina.cerberus.machineinfo;

import java.io.IOException;
import java.util.Scanner;

public class WindowsMachineInfo extends AbstractMachineInfo {
    /**
     * 获取CPU型号
     *
     * @return CPU型号
     */
    @Override
    public String getCpu() {
        String cmd = "wmic cpu get processorid";
        return this.runWindowsCommandLine(cmd);
    }

    /**
     * 获取主机序列号
     *
     * @return 序列号
     */
    @Override
    public String getSerial() {
        String cmd = "wmic baseboard get serialnumber";
        return this.runWindowsCommandLine(cmd);
    }

    /**
     * 运行命令行
     *
     * @param shell 命令
     * @return 结果
     */
    String runWindowsCommandLine(String shell) {
        String serialNumber = "UNKNOWN";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(shell);
            process.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(process.getInputStream());

        if (scanner.hasNext()) {
            scanner.next();
        }

        if (scanner.hasNext()) {
            serialNumber = scanner.next().trim();
        }

        scanner.close();
        return serialNumber;
    }
}
