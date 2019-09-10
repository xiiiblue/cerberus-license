package cn.telchina.cerberus.machineinfo;

import cn.telchina.cerberus.machineinfo.LinuxMachineInfo;
import cn.telchina.cerberus.machineinfo.MachineInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class LinuxMachineInfoTest {
    private MachineInfo machineInfo = new LinuxMachineInfo();

    @Test
    public void getCPUSerial() {
        String result = machineInfo.getCpu();

        log.info("result: {}", result);
        assertNotNull(result);
    }

    @Test
    public void getMainBoardSerial() {
        String result = machineInfo.getSerial();

        log.info("result: {}", result);
        assertNotNull(result);
    }
}