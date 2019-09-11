package cn.telchina.cerberus;

import cn.telchina.cerberus.fingerprint.FingerprintManager;
import cn.telchina.cerberus.pojo.Fingerprint;
import org.apache.commons.cli.*;

public class FingerprintCLI {
    private static FingerprintManager fingerprintManager;

    private static final String HELP_LONG = "help";
    private static final String HELP_SHORT = "h";
    private static final String DEBUG_LONG = "debug";
    private static final String DEBUG_SHORT = "d";


    static {
        fingerprintManager = new FingerprintManager();
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder(HELP_SHORT).longOpt(HELP_LONG).desc("show command help").build());
        options.addOption(Option.builder(DEBUG_SHORT).longOpt(DEBUG_LONG).desc("show debug log").build());
        return options;
    }

    private static void printHelp() {
        Options options = buildOptions();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar ${jar} [options]", options);
        System.out.println();
        System.out.println("Example:");
        System.out.println("java -jar cerberus-license-fingerprint-cli-1.0.0.jar");
    }

    private static void printMachineInfo() {
        Fingerprint machine = fingerprintManager.getFingerprint();
        String mac = machine.getMac();
        String ip = machine.getIp();
        String cpu = machine.getCpu();
        String serial = machine.getSerial();

        System.out.println();
        System.out.println();
        System.out.println("Machine infomation:");
        System.out.println("----------------------------------------------------------------");
        System.out.println("mac    : " + mac);
        System.out.println("ip     : " + ip);
        System.out.println("cpu    : " + cpu);
        System.out.println("serial : " + serial);
        System.out.println("----------------------------------------------------------------");
        System.out.println();
    }

    private static void printFingerprint() {

        String encode = fingerprintManager.encode();
        System.out.println();
        System.out.println("Fingerprint:");
        System.out.println("----------------------------------------------------------------");
        System.out.println(encode);
        System.out.println("----------------------------------------------------------------");

    }


    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder(HELP_SHORT).longOpt(HELP_LONG).desc("show command help").build());
        options.addOption(Option.builder(DEBUG_SHORT).longOpt(DEBUG_LONG).desc("show debug log").build());

        CommandLine commandLine = null;
        try {
            commandLine = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (commandLine.hasOption(HELP_LONG)) {
            printHelp();
            System.exit(0);
        }

        printFingerprint();

        if (commandLine.hasOption(DEBUG_LONG)) {
            printMachineInfo();
        }
    }
}
