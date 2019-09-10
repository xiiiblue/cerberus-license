package cn.telchina.cerberus.util;

import cn.telchina.cerberus.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

import static org.junit.Assert.*;

@Slf4j
public class RSAUtilsTest {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Before
    public void prepare() throws NoSuchAlgorithmException {
        KeyPair keyPair = RSAUtils.generateKey();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    @Test
    public void generateKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = RSAUtils.generateKey();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        byte[] publicKey = keyPair.getPublic().getEncoded();

        String privateKeyEnc = Base64.getEncoder().encodeToString(privateKey);
        String publicKeyEnc = Base64.getEncoder().encodeToString(publicKey);

        log.info("privateKeyEnc: {}", privateKeyEnc);
        log.info("publicKeyEnc: {}", publicKeyEnc);
        assertNotNull(privateKeyEnc);
        assertNotNull(publicKeyEnc);
    }

    @Test
    public void encryptAndDecrypt() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        String data = "foo bar";

        byte[] encrypt = RSAUtils.encrypt(this.publicKey, data.getBytes());
        byte[] decrypt = RSAUtils.decrypt(privateKey, encrypt);

        assertEquals(new String(decrypt), data);
    }

    @Test
    public void signAndVerify() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String data = "foo bar";
        String sign = RSAUtils.sign(this.privateKey, data);
        assertNotNull(sign);

        boolean verify = RSAUtils.verify(this.publicKey, data, sign);
        assertTrue(verify);
    }
}