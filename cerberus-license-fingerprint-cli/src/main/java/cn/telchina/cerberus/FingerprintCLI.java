package cn.telchina.cerberus;

import cn.telchina.cerberus.fingerprint.FingerprintManager;

public class FingerprintCLI {
    public static void main(String[] args) {
        FingerprintManager fingerprintManager = new FingerprintManager();
        String encode = fingerprintManager.encode();
        System.out.println("Fingerprint:");
        System.out.println(encode);
    }
}
