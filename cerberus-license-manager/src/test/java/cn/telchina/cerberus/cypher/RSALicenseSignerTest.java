package cn.telchina.cerberus.cypher;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.file.Paths;

@Slf4j
public class RSALicenseSignerTest {

    @Test
    public void generateKeyPair() {
        RSALicenseSigner signer = new RSALicenseSigner();
        signer.generate();
        log.info("privateKey {}", signer.getPrivateKey());
        log.info("publicKey {}", signer.getPublicKey());

        String sign = signer.sign("foobar");
        log.info("sign: {}", sign);

        boolean verify = signer.verify("foobar", sign);
        log.info("verify {}", verify);
    }

    @Test
    public void setKeyPair() {
        RSALicenseSigner signer = new RSALicenseSigner();
        signer.setPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCJZXzz05bOV/LFIsMaaVMebfGo+hB9mLA0J001TYYcvJH4dEtNuJvDjem80R1j3Tz0dn0FyCGYA9mfunFm9iyXnfEgPoLPtkuJffxzNPpQsb1d66LAlOOGNzkCSmdjK7cfiKLPeCgHyLGwuDvf/YBZ8XfPpomLzWm8WtzTReKBF4PsyEwEdS26VIYHTNLn3DzteLYBoN+AtiI8MhWbJl0lNkoNIK8jpGKTw+9O/4IbMM5ucqF9Asx9rqR/WCDlEFYEs9yQy/xB6dSvOVN9Z1zyDImhuctm+HCs02QKSOZMOIrTSFU2OuYXLG6eUSvS5VXVz26iE8c1GuC+J0NTPiCPAgMBAAECggEADscstmkWumD5SrzfcVLMzuQSbxNefLPUl/d2NXp+J08dADU5+EUX8+OqtVVkrN4Z9U1dSybofhpD6Qs7sVBfIXuqeMZgeYjo/QPhCF81YUKM1LJIkiAxjLkZ16Y7Eo6cJrcik+Afph6vBgv63K++g24Wbe5CsxfH/KtlPxAwOaQuXSPxrac/DVtoajn1qa30lCzVFGbulbYFUVZCZWlhOhq1hrzOoqFHx4zXVIxhImtrOTf8Xyw2C77bD0XbwhtzkKavJyrMvxwfoeZ229Eucu3ubLfucwr6OC2Y+thiBuujvY2l1OZJq2oY3RdgJcSYKkFVJ2j+qNsftvmFg0uzaQKBgQDMWO08HJvzhDKfAbDPPl45yOag8W/FfTgR32DBO0xMdZ59KUQN/xHkNxjkJu7E6LM+0RFcIu3KEASTRmzOu0Ok6Bv0Y3maGC2nkz2tbMwOaTPwGvfS6Ka0LHvgL3FKSFZePpEV9+YfkqvX1IJKPvkWHAA1WfA1vIL0eadXYUR8MwKBgQCsID8x/F7ZXixxVMRwe0Y4O98QxPEvDeS7S2/mdEUtgRKsxPrni8nvYyV5Ja3Sjx2Wa1PGmVyefCiRMxM7gKmoWwN8UgD/7aKCYZl99eAw2/+qBvHLWiHAdEqLSKb8PqgmzKqhiMbvo7UOqiF+f8//H3rFeb4rBpC2HXM6sAPuNQKBgEvIuwqtDYUtRNzFcr4ZQjap9CZCBBK40r+GGUrwY+aDRGjkkfGi5A7ABEIw0iJCrp8gSBDkf02NzTSVTKsKthaFYkCrV6C3UM8yAxC7JZ4+k917EuesEo8FZFeLILfMxgMjrwj3q+ePrJ1ZmYxReG0jq0wd88DqDaL/LvlwAEcTAoGAZ7FRgNJxbW+fRHL1mHGbPttKqXaLeXZcOjza37Fhz2T3lB9iq8T51P4coBwD2FohT/G+WWEge9V+NuLXDjyXeHXD5swcEBHfmb4kUs6hza1rGsnuNjxJIwCru7b76e/xKtaXYJLejZVIyNnTLbrf2ejj9D1AQ/lDioK5XTyWUZUCgYAnV8XXu65tTIDpdiBB5NBnpLyr9q0vEQ8CtYOfF6oDlZk+jWQzyLYeZraFsy7PZ1KL5KNgAFwhb5os5bSztuTKBQ4lq+aNaGvAaDCJkLhui8JXyf4zrxj7CT9YzwpLrs994J2b58GgSc2c7jMa1Xy9rlvnSARF5/J/1NVLRvQDmw==");
        signer.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiWV889OWzlfyxSLDGmlTHm3xqPoQfZiwNCdNNU2GHLyR+HRLTbibw43pvNEdY9089HZ9BcghmAPZn7pxZvYsl53xID6Cz7ZLiX38czT6ULG9XeuiwJTjhjc5AkpnYyu3H4iiz3goB8ixsLg73/2AWfF3z6aJi81pvFrc00XigReD7MhMBHUtulSGB0zS59w87Xi2AaDfgLYiPDIVmyZdJTZKDSCvI6Rik8PvTv+CGzDObnKhfQLMfa6kf1gg5RBWBLPckMv8QenUrzlTfWdc8gyJobnLZvhwrNNkCkjmTDiK00hVNjrmFyxunlEr0uVV1c9uohPHNRrgvidDUz4gjwIDAQAB");

        String sign = signer.sign("foobar");
        log.info("sign: {}", sign);

        boolean verify = signer.verify("foobar", sign);
        log.info("verify {}", verify);
    }


    @Test
    public void saveKeyPairToFile() {
        RSALicenseSigner signer = new RSALicenseSigner();
        signer.generate();

        signer.savePrivateKey(Paths.get("/tmp/private.key"));
        signer.savePublicKey(Paths.get("/tmp/public.key"));
    }

    @Test
    public void loadKeyPairFromFile() {
        RSALicenseSigner signer = new RSALicenseSigner();

        signer.loadPrivateKey(Paths.get("/tmp/private.key"));
        signer.loadPublicKey(Paths.get("/tmp/public.key"));
    }
}