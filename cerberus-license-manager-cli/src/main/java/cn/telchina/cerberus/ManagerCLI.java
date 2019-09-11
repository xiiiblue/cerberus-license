package cn.telchina.cerberus;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.telchina.cerberus.fingerprint.FingerprintManager;
import cn.telchina.cerberus.license.LicenseBuilder;
import org.apache.commons.cli.*;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class ManagerCLI {
    private static FingerprintManager fingerprintManager;
    private static LicenseBuilder licenseBuilder;

    private static final String HELP_LONG = "help";
    private static final String HELP_SHORT = "h";
    private static final String DEBUG_LONG = "debug";
    private static final String DEBUG_SHORT = "d";
    private static final String TYPE_LONG = "type";
    private static final String TYPE_SHORT = "t";
    private static final String SUBJECT_LONG = "subject";
    private static final String SUBJECT_SHORT = "s";
    private static final String PERIOD_LONG = "period";
    private static final String PERIOD_SHORT = "p";
    private static final String FINGERPRINT_LONG = "fingerprint";
    private static final String FINGERPRINT_SHORT = "f";
    private static final String EXTRA_LONG = "extra";
    private static final String EXTRA_SHORT = "e";

    static {
        String privateKey = ResourceBundle.getBundle("config").getString("privateKey");
        licenseBuilder = new LicenseBuilder().setPrivateKey(privateKey).init();
        fingerprintManager = new FingerprintManager();
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder(HELP_SHORT).longOpt(HELP_LONG).desc("show command help").build());
        options.addOption(Option.builder(DEBUG_SHORT).longOpt(DEBUG_LONG).desc("show debug log").build());
        options.addOption(Option.builder(SUBJECT_SHORT).longOpt(SUBJECT_LONG).hasArg().desc("set license subject").build());
        options.addOption(Option.builder(TYPE_SHORT).longOpt(TYPE_LONG).hasArg().desc("setauth type").build());
        options.addOption(Option.builder(PERIOD_SHORT).longOpt(PERIOD_LONG).hasArg().desc("set period of validity(months)").build());
        options.addOption(Option.builder(FINGERPRINT_SHORT).longOpt(FINGERPRINT_LONG).hasArg().desc("set machine finger print").build());
        options.addOption(Option.builder(EXTRA_SHORT).longOpt(EXTRA_LONG).hasArg().desc("set extra infomation, such as function list").build());
        return options;
    }

    private static void printHelp() {
        Options options = buildOptions();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar ${jar} [options]", options);

        System.out.println();
        System.out.println("'-f,--fingerprint' is optional, if you leave it blank, will generate the fingerprint of current machine");
        System.out.println();
        System.out.println("Example:");
        System.out.println("java -jar cerberus-license-manager-cli-1.0.0.jar --subject=yourproject --type=BASIC --period=36");
        System.out.println("java -jar cerberus-license-manager-cli-1.0.0.jar --subject=yourproject --type=BASIC --period=36 --fingerprint=foobar");
    }

    private static void enableDebugLog() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("ROOT").setLevel(Level.valueOf("DEBUG"));
    }

    private static CommandLine parseArguments(String[] arguments) {
        Options options = buildOptions();
        CommandLine commandLine = null;
        try {
            commandLine = new DefaultParser().parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (commandLine.getOptions().length == 0 || commandLine.hasOption(HELP_LONG)) {
            printHelp();
            System.exit(0);
        }

        if (commandLine.hasOption(DEBUG_LONG)) {
            enableDebugLog();
        }

        return commandLine;
    }


    public static void main(String[] args) {
        CommandLine commandLine = parseArguments(args);
        String subject = commandLine.getOptionValue(SUBJECT_LONG);
        String type = commandLine.getOptionValue(TYPE_LONG);
        String period = commandLine.getOptionValue(PERIOD_LONG);
        String fingerprint = commandLine.getOptionValue(FINGERPRINT_LONG);
        String extra = commandLine.getOptionValue(EXTRA_LONG);

        if (subject == null) {
            System.out.println("Missing required options: subject");
            System.exit(1);
        }
        if (type == null) {
            System.out.println("Missing required options: type");
            System.exit(1);
        }
        if (period == null) {
            System.out.println("Missing required options: period");
            System.exit(1);
        }
        if (extra == null) extra = "";

        Integer periodInt = 0;
        try {
            periodInt = Integer.valueOf(period);
        } catch (NumberFormatException e) {
            System.out.println("The type of 'period' must be Integer!");
            System.exit(1);
        }

        if (fingerprint == null) {
            System.out.println("Fingerprint not assigned, will collect the fingerprint of this machine");
            fingerprint = fingerprintManager.encode();
        }

        System.out.println();
        System.out.println("Please confirm the license infomation");
        System.out.println("-------------------------------------");
        System.out.println("subject     : " + subject);
        System.out.println("type        : " + type);
        System.out.println("period      : " + period);
        System.out.println("fingerprint : " + fingerprint);
        System.out.println("extra       : " + extra);
        System.out.println("-------------------------------------");
        System.out.println();
        System.out.println("Begin to generate license... ");
        System.out.println();

        String license = licenseBuilder
                .setSubject(subject)
                .setAuthType(type)
                .setExpireDate(periodInt)
                .setFingerprint(fingerprint)
                .build();

        System.out.println(license);
    }
}
