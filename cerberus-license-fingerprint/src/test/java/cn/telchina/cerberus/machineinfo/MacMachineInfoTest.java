package cn.telchina.cerberus.machineinfo;

import cn.telchina.cerberus.machineinfo.MacMachineInfo;
import cn.telchina.cerberus.machineinfo.MachineInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class MacMachineInfoTest {
    private MachineInfo machineInfo = new MacMachineInfo();

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