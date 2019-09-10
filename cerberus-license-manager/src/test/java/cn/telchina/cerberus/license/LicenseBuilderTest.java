package cn.telchina.cerberus.license;

import cn.telchina.cerberus.fingerprint.FingerprintManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Slf4j
public class LicenseBuilderTest {
    private FingerprintManager fingerprintManager = new FingerprintManager();
    private String privateKey;
    private String fingerprint;

    @Before
    public void prepare() {
        this.privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCJZXzz05bOV/LFIsMaaVMebfGo+hB9mLA0J001TYYcvJH4dEtNuJvDjem80R1j3Tz0dn0FyCGYA9mfunFm9iyXnfEgPoLPtkuJffxzNPpQsb1d66LAlOOGNzkCSmdjK7cfiKLPeCgHyLGwuDvf/YBZ8XfPpomLzWm8WtzTReKBF4PsyEwEdS26VIYHTNLn3DzteLYBoN+AtiI8MhWbJl0lNkoNIK8jpGKTw+9O/4IbMM5ucqF9Asx9rqR/WCDlEFYEs9yQy/xB6dSvOVN9Z1zyDImhuctm+HCs02QKSOZMOIrTSFU2OuYXLG6eUSvS5VXVz26iE8c1GuC+J0NTPiCPAgMBAAECggEADscstmkWumD5SrzfcVLMzuQSbxNefLPUl/d2NXp+J08dADU5+EUX8+OqtVVkrN4Z9U1dSybofhpD6Qs7sVBfIXuqeMZgeYjo/QPhCF81YUKM1LJIkiAxjLkZ16Y7Eo6cJrcik+Afph6vBgv63K++g24Wbe5CsxfH/KtlPxAwOaQuXSPxrac/DVtoajn1qa30lCzVFGbulbYFUVZCZWlhOhq1hrzOoqFHx4zXVIxhImtrOTf8Xyw2C77bD0XbwhtzkKavJyrMvxwfoeZ229Eucu3ubLfucwr6OC2Y+thiBuujvY2l1OZJq2oY3RdgJcSYKkFVJ2j+qNsftvmFg0uzaQKBgQDMWO08HJvzhDKfAbDPPl45yOag8W/FfTgR32DBO0xMdZ59KUQN/xHkNxjkJu7E6LM+0RFcIu3KEASTRmzOu0Ok6Bv0Y3maGC2nkz2tbMwOaTPwGvfS6Ka0LHvgL3FKSFZePpEV9+YfkqvX1IJKPvkWHAA1WfA1vIL0eadXYUR8MwKBgQCsID8x/F7ZXixxVMRwe0Y4O98QxPEvDeS7S2/mdEUtgRKsxPrni8nvYyV5Ja3Sjx2Wa1PGmVyefCiRMxM7gKmoWwN8UgD/7aKCYZl99eAw2/+qBvHLWiHAdEqLSKb8PqgmzKqhiMbvo7UOqiF+f8//H3rFeb4rBpC2HXM6sAPuNQKBgEvIuwqtDYUtRNzFcr4ZQjap9CZCBBK40r+GGUrwY+aDRGjkkfGi5A7ABEIw0iJCrp8gSBDkf02NzTSVTKsKthaFYkCrV6C3UM8yAxC7JZ4+k917EuesEo8FZFeLILfMxgMjrwj3q+ePrJ1ZmYxReG0jq0wd88DqDaL/LvlwAEcTAoGAZ7FRgNJxbW+fRHL1mHGbPttKqXaLeXZcOjza37Fhz2T3lB9iq8T51P4coBwD2FohT/G+WWEge9V+NuLXDjyXeHXD5swcEBHfmb4kUs6hza1rGsnuNjxJIwCru7b76e/xKtaXYJLejZVIyNnTLbrf2ejj9D1AQ/lDioK5XTyWUZUCgYAnV8XXu65tTIDpdiBB5NBnpLyr9q0vEQ8CtYOfF6oDlZk+jWQzyLYeZraFsy7PZ1KL5KNgAFwhb5os5bSztuTKBQ4lq+aNaGvAaDCJkLhui8JXyf4zrxj7CT9YzwpLrs994J2b58GgSc2c7jMa1Xy9rlvnSARF5/J/1NVLRvQDmw==";
        log.info("privateKey: {}", this.privateKey);

        this.fingerprint = fingerprintManager.encode();
        log.info("fingerprint: {}", this.fingerprint);
    }

    @Test
    public void exportToConsole() {
        String license = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystemName")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .build();

        log.info("license: {}", license);
        assertNotNull(license);
    }

    @Test
    public void exportToFile() {
        String path = "/tmp/license.lic";
        new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystemName")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .export(Paths.get(path));

        assertTrue(new File(path).isFile());
    }
}