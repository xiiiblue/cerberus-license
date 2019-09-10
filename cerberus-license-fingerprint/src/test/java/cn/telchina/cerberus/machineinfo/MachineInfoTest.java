package cn.telchina.cerberus.machineinfo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class MachineInfoTest {
    private MachineInfo machineInfo = new MacMachineInfo();

    @Test
    public void getIpAddress() {
        String result = machineInfo.getIp();

        log.info("result: {}", result);
        assertNotNull(result);
    }

    @Test
    public void getMacAddress() {
        String result = machineInfo.getMac();

        log.info("result: {}", result);
        assertNotNull(result);
    }

    public static void main(String[] args) {
        String s = new Date().toInstant().toString();
        System.out.println(s);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("k1", "v1");
        map.put("k3", "v3");
        map.put("k2", "v2");


        String collect = map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.joining("|"));

        System.out.println(collect);


    }
}