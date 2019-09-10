package cn.telchina.cerberus.machineinfo;

public class WindowsMachineInfo extends AbstractMachineInfo {
    /**
     * 获取CPU型号
     *
     * @return CPU型号
     */
    @Override
    public String getCpu() {
        String[] shell = {"wmic cpu get processorid"};
        return this.runCommandLine(shell);
    }

    /**
     * 获取主机序列号
     *
     * @return 序列号
     */
    @Override
    public String getSerial() {
        String[] shell = {"wmic cpu get processorid"};
        return this.runCommandLine(shell);
    }
}
