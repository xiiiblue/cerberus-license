package cn.telchina.cerberus.machineinfo;

public class LinuxMachineInfo extends AbstractMachineInfo {
    /**
     * 获取CPU型号
     *
     * @return CPU型号
     */
    @Override
    public String getCpu() {
        String[] shell = {"/bin/bash", "-c", "dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
        return this.runCommandLine(shell);
    }

    /**
     * 获取主机序列号
     *
     * @return 序列号
     */
    @Override
    public String getSerial() {
        String[] shell = {"/bin/bash", "-c", "dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
        return this.runCommandLine(shell);
    }
}
