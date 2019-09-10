package cn.telchina.cerberus.fingerprint;

import cn.telchina.cerberus.pojo.Fingerprint;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class FingerprintManagerTest {
    private FingerprintManager fingerprintManager = new FingerprintManager();

    @Test
    public void create() {
        String encode = fingerprintManager.encode();

        log.info("encode: {}", encode);
        assertNotNull(encode);

        Fingerprint fingerprint = fingerprintManager.getFingerprint();
        log.info("fingerprint: {}", fingerprint);
    }
}