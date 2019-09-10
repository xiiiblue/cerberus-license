package cn.telchina.cerberus.license;

import cn.telchina.cerberus.cypher.RSALicenseSigner;
import cn.telchina.cerberus.exception.LicenseVerifyException;
import cn.telchina.cerberus.fingerprint.FingerprintManager;
import cn.telchina.cerberus.pojo.License;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
public class LicenseVerifierTest {
    private FingerprintManager fingerprintManager = new FingerprintManager();
    private String privateKey;
    private String publicKey;
    private String fingerprint;

    @Before
    public void prepare() {
        log.info("prepare start...");
        this.privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCJZXzz05bOV/LFIsMaaVMebfGo+hB9mLA0J001TYYcvJH4dEtNuJvDjem80R1j3Tz0dn0FyCGYA9mfunFm9iyXnfEgPoLPtkuJffxzNPpQsb1d66LAlOOGNzkCSmdjK7cfiKLPeCgHyLGwuDvf/YBZ8XfPpomLzWm8WtzTReKBF4PsyEwEdS26VIYHTNLn3DzteLYBoN+AtiI8MhWbJl0lNkoNIK8jpGKTw+9O/4IbMM5ucqF9Asx9rqR/WCDlEFYEs9yQy/xB6dSvOVN9Z1zyDImhuctm+HCs02QKSOZMOIrTSFU2OuYXLG6eUSvS5VXVz26iE8c1GuC+J0NTPiCPAgMBAAECggEADscstmkWumD5SrzfcVLMzuQSbxNefLPUl/d2NXp+J08dADU5+EUX8+OqtVVkrN4Z9U1dSybofhpD6Qs7sVBfIXuqeMZgeYjo/QPhCF81YUKM1LJIkiAxjLkZ16Y7Eo6cJrcik+Afph6vBgv63K++g24Wbe5CsxfH/KtlPxAwOaQuXSPxrac/DVtoajn1qa30lCzVFGbulbYFUVZCZWlhOhq1hrzOoqFHx4zXVIxhImtrOTf8Xyw2C77bD0XbwhtzkKavJyrMvxwfoeZ229Eucu3ubLfucwr6OC2Y+thiBuujvY2l1OZJq2oY3RdgJcSYKkFVJ2j+qNsftvmFg0uzaQKBgQDMWO08HJvzhDKfAbDPPl45yOag8W/FfTgR32DBO0xMdZ59KUQN/xHkNxjkJu7E6LM+0RFcIu3KEASTRmzOu0Ok6Bv0Y3maGC2nkz2tbMwOaTPwGvfS6Ka0LHvgL3FKSFZePpEV9+YfkqvX1IJKPvkWHAA1WfA1vIL0eadXYUR8MwKBgQCsID8x/F7ZXixxVMRwe0Y4O98QxPEvDeS7S2/mdEUtgRKsxPrni8nvYyV5Ja3Sjx2Wa1PGmVyefCiRMxM7gKmoWwN8UgD/7aKCYZl99eAw2/+qBvHLWiHAdEqLSKb8PqgmzKqhiMbvo7UOqiF+f8//H3rFeb4rBpC2HXM6sAPuNQKBgEvIuwqtDYUtRNzFcr4ZQjap9CZCBBK40r+GGUrwY+aDRGjkkfGi5A7ABEIw0iJCrp8gSBDkf02NzTSVTKsKthaFYkCrV6C3UM8yAxC7JZ4+k917EuesEo8FZFeLILfMxgMjrwj3q+ePrJ1ZmYxReG0jq0wd88DqDaL/LvlwAEcTAoGAZ7FRgNJxbW+fRHL1mHGbPttKqXaLeXZcOjza37Fhz2T3lB9iq8T51P4coBwD2FohT/G+WWEge9V+NuLXDjyXeHXD5swcEBHfmb4kUs6hza1rGsnuNjxJIwCru7b76e/xKtaXYJLejZVIyNnTLbrf2ejj9D1AQ/lDioK5XTyWUZUCgYAnV8XXu65tTIDpdiBB5NBnpLyr9q0vEQ8CtYOfF6oDlZk+jWQzyLYeZraFsy7PZ1KL5KNgAFwhb5os5bSztuTKBQ4lq+aNaGvAaDCJkLhui8JXyf4zrxj7CT9YzwpLrs994J2b58GgSc2c7jMa1Xy9rlvnSARF5/J/1NVLRvQDmw==";
        this.publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiWV889OWzlfyxSLDGmlTHm3xqPoQfZiwNCdNNU2GHLyR+HRLTbibw43pvNEdY9089HZ9BcghmAPZn7pxZvYsl53xID6Cz7ZLiX38czT6ULG9XeuiwJTjhjc5AkpnYyu3H4iiz3goB8ixsLg73/2AWfF3z6aJi81pvFrc00XigReD7MhMBHUtulSGB0zS59w87Xi2AaDfgLYiPDIVmyZdJTZKDSCvI6Rik8PvTv+CGzDObnKhfQLMfa6kf1gg5RBWBLPckMv8QenUrzlTfWdc8gyJobnLZvhwrNNkCkjmTDiK00hVNjrmFyxunlEr0uVV1c9uohPHNRrgvidDUz4gjwIDAQAB";
        this.fingerprint = fingerprintManager.encode();
        log.info("fingerprint: {}", this.fingerprint);
        log.info("prepare finish!");
    }

    @Test
    public void checkTest() {
        String licenseEnc = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystem")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .build();
        log.info("licenseEnc: {}", licenseEnc);

        LicenseVerifier verifier = new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);


        License license = verifier.getLicense();
        log.info("license: {}", license);

        boolean checkSign = verifier.checkSign();
        log.info("checkSign: {}", checkSign);
        assertTrue(checkSign);

        boolean checkExpire = verifier.checkExpire();
        log.info("checkExpire: {}", checkExpire);
        assertTrue(checkExpire);

        boolean checkFingerprint = verifier.checkFingerprint();
        log.info("checkFingerprint: {}", checkFingerprint);
        assertTrue(checkFingerprint);
    }

    @Test(expected = LicenseVerifyException.class)
    public void fakeLicense() {
        String licenseEnc = "foobar";
        log.info("String licenseEnc: {}", licenseEnc);

        new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);
    }


    @Test(expected = LicenseVerifyException.class)
    public void emptySign() {
        String licenseEnc = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystem")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .buildWithoutSign();
        log.info("licenseEnc: {}", licenseEnc);

        LicenseVerifier verifier = new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);

        boolean checkSign = verifier.checkSign();
        log.info("checkSign: {}", checkSign);
        assertFalse(checkSign);
    }

    @Test(expected = LicenseVerifyException.class)
    public void inccrectSign() {
        String licenseEnc = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystem")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .setSign("foobar")
                .buildWithoutSign();
        log.info("licenseEnc: {}", licenseEnc);

        LicenseVerifier verifier = new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);

        boolean checkSign = verifier.checkSign();
        log.info("checkSign: {}", checkSign);
        assertFalse(checkSign);
    }


    @Test
    public void nonMatchedSign() {
        String licenseEnc = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystem")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .setSign("Mi1LiZaGOSj0/e0k01kc8Glt77J1dMYfQZYMhyrGRerWE1w1TecZsp2qdG4y3zbh9lJmedkNi1qpnE/Vt4hLRzilM1pXeD9xU+gWHyMf9E9fytyvXaZJhbx+Shm+uCWibHNKb7T/rJu99aeLIlHJj9ikXig+951Q0hdwJHSDTfaS+iAweYqDYd5xemF7BBscrfQDnvoGizDQM5gb/8VXY4x7N6vbkr5kTiYHZ6EtoEfi/ZDQNRP95u653J0WY44gmJugNJ8jBfXGvsla+uRkt852mtP0FHRmrZDEUuMYCg4+R45g4lJKmsKaoPX4i/39zTHmIiqwNuYjhtHawVz9vg==")
                .buildWithoutSign();
        log.info("licenseEnc: {}", licenseEnc);

        LicenseVerifier verifier = new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);

        boolean checkSign = verifier.checkSign();
        log.info("checkSign: {}", checkSign);
        assertFalse(checkSign);
    }


    @Test
    public void expiredLicense() {
        String licenseEnc = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystem")
                .setExpireDate(-1)
                .setFingerprint(this.fingerprint)
                .build();
        log.info("licenseEnc: {}", licenseEnc);

        LicenseVerifier verifier = new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);

        boolean checkExpire = verifier.checkExpire();
        log.info("checkExpire: {}", checkExpire);
        assertFalse(checkExpire);
    }

    @Test
    public void inccrectFingerprint() {
        String licenseEnc = new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setAuthType("BASIC")
                .setSubject("YourSystem")
                .setExpireDate(24)
                .setFingerprint("foobar")
                .build();
        log.info("licenseEnc: {}", licenseEnc);

        LicenseVerifier verifier = new LicenseVerifier()
                .setPublicKey(this.publicKey)
                .setLicense(licenseEnc);

        boolean checkFingerprint = verifier.checkFingerprint();
        log.info("checkFingerprint: {}", checkFingerprint);
        assertFalse(checkFingerprint);
    }


    @Test
    public void loadLicenseFromFile() {
        String privateKeyPath = "/tmp/private.key";
        String publicKeyPath = "/tmp/public.key";
        String licensePath = "/tmp/license.lic";

        RSALicenseSigner signer = new RSALicenseSigner();
        signer.generate();
        signer.savePrivateKey(Paths.get(privateKeyPath));
        signer.savePublicKey(Paths.get(publicKeyPath));

        new LicenseBuilder()
                .loadPrivateKey(Paths.get(privateKeyPath))
                .init()
                .setSubject("YourSystemName")
                .setExpireDate(24)
                .setFingerprint(this.fingerprint)
                .export(Paths.get(licensePath));

        new LicenseVerifier()
                .loadPublicKey(Paths.get(publicKeyPath))
                .loadLicense(Paths.get(licensePath));
    }
}