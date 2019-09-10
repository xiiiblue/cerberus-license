package cn.telchina.cerberus.fingerprint;

import cn.telchina.cerberus.cypher.Hash;
import cn.telchina.cerberus.exception.FingerprintException;
import cn.telchina.cerberus.machineinfo.LinuxMachineInfo;
import cn.telchina.cerberus.machineinfo.MacMachineInfo;
import cn.telchina.cerberus.machineinfo.MachineInfo;
import cn.telchina.cerberus.machineinfo.WindowsMachineInfo;
import cn.telchina.cerberus.pojo.Fingerprint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 硬件指纹管理类
 */
@Slf4j
public class FingerprintManager {
    private MachineInfo machineInfo;
    private Fingerprint fingerprint = new Fingerprint();
    private ObjectMapper objectMapper = new ObjectMapper();

    public FingerprintManager() {
        String os = System.getProperty("os.name").toLowerCase();
        log.debug("os: {}", os);

        if (os.startsWith("windows")) {
            this.machineInfo = new WindowsMachineInfo();
        } else if (os.startsWith("mac")) {
            this.machineInfo = new MacMachineInfo();
        } else {
            this.machineInfo = new LinuxMachineInfo();
        }
    }

    public FingerprintManager(MachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }

    /**
     * 生成指纹
     *
     * @return 指纹编码
     */
    public String encode() {
        collectFingerprint();
        return hash(this.fingerprint);
    }

    /**
     * 校验指纹
     *
     * @param fingerprint 指纹
     * @return boolean
     */
    public boolean verify(String fingerprint) {
        return encode().equals(fingerprint);
    }


    /**
     * 设置Fingerprint实例
     *
     * @param fingerprint 机器指纹
     */
    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * 获取Fingerprint实例
     *
     * @return 硬件指纹
     */
    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    /**
     * 采集机器指纹
     */
    private void collectFingerprint() {
        fingerprint.setMac(machineInfo.getMac());
        fingerprint.setIp(machineInfo.getIp());
        fingerprint.setCpu(machineInfo.getCpu());
        fingerprint.setSerial(machineInfo.getSerial());
    }

    /**
     * Sha3哈希
     *
     * @return 哈希值
     */
    private String hash(Fingerprint fingerprint) {
        try {
            String json = objectMapper.writeValueAsString(fingerprint);
            return Hash.sha3String(json);
        } catch (JsonProcessingException e) {
            log.error("context", e);
            throw new FingerprintException("指纹序列化失败");
        }
    }
}
