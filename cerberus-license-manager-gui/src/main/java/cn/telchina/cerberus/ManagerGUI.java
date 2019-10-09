package cn.telchina.cerberus;

import cn.telchina.cerberus.fingerprint.FingerprintManager;
import cn.telchina.cerberus.license.LicenseBuilder;
import com.google.common.base.Strings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

public class ManagerGUI {
    private JPanel licenseManager;
    private JTextField textFieldSubject;
    private JTextField textFieldType;
    private JTextField textFieldPeriod;
    private JTextField textFieldFingerprint;
    private JTextField textFieldExtra;
    private JButton generateLicenseButton;
    private JTextField textFieldLicense;

    private static FingerprintManager fingerprintManager;
    private static LicenseBuilder licenseBuilder;

    static {
        String privateKey = ResourceBundle.getBundle("config").getString("privateKey");
        licenseBuilder = new LicenseBuilder().setPrivateKey(privateKey).init();
        fingerprintManager = new FingerprintManager();
    }

    public ManagerGUI() {
        generateLicenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subject = textFieldSubject.getText();
                String type = textFieldType.getText();
                String period = textFieldPeriod.getText();
                String fingerprint = textFieldFingerprint.getText();
                String extra = textFieldExtra.getText();

                if (Strings.isNullOrEmpty(subject)) {
                    showMessageDialog(licenseManager, "Missing required options: subject");
                    return;
                }
                if (Strings.isNullOrEmpty(type)) {
                    showMessageDialog(licenseManager, "Missing required options: type");
                    return;
                }
                if (Strings.isNullOrEmpty(period)) {
                    showMessageDialog(licenseManager, "Missing required options: period");
                    return;
                }
                Integer periodInt = Integer.valueOf(period);

                if (Strings.isNullOrEmpty(fingerprint)) {
                    showMessageDialog(licenseManager, "Fingerprint not assigned, will collect the fingerprint of this machine");
                    String license = fingerprintManager.encode();
                    textFieldFingerprint.setText(license);
                }

                String license = licenseBuilder
                        .setSubject(subject)
                        .setAuthType(type)
                        .setExpireDate(periodInt)
                        .setFingerprint(fingerprint)
                        .setExtra(extra)
                        .build();
                textFieldLicense.setText(license);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ManagerGUI");
        frame.setContentPane(new ManagerGUI().licenseManager);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(480, 320);
        frame.setVisible(true);
    }
}
