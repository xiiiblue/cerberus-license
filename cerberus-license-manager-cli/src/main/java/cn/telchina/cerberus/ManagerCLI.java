package cn.telchina.cerberus;

import cn.telchina.cerberus.fingerprint.FingerprintManager;
import cn.telchina.cerberus.license.LicenseBuilder;
import org.apache.commons.cli.*;

import java.util.ResourceBundle;

public class ManagerCLI {
    private FingerprintManager fingerprintManager;
    private String privateKey;

    private static final String SOURCE_PATH = "./task-core/src/test/java/com/git/hui/task";
    private static final String TASK_ARG_LONG = "task";
    private static final String TASK_ARG_SHORT = "t";
    private static final String ARG_HELP_LONG = "help";
    private static final String ARG_HELP_SHORT = "h";
    private static volatile boolean run = true;

    public ManagerCLI() {
        this.privateKey = ResourceBundle.getBundle("config").getString("privateKey");
        this.fingerprintManager = new FingerprintManager();
    }

    public String generateLicense(String subject, String authType, Integer monthDuration, String fingerprint) {
        return new LicenseBuilder()
                .setPrivateKey(this.privateKey)
                .init()
                .setSubject(subject)
                .setAuthType(authType)
                .setExpireDate(monthDuration)
                .setFingerprint(fingerprint)
                .build();
    }

    public String generateLicense(String subject, String authType, Integer monthDuration) {
        String fingerprint = this.fingerprintManager.encode();
        return generateLicense(subject, authType, monthDuration, fingerprint);
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder(TASK_ARG_SHORT).argName(TASK_ARG_LONG).hasArg().longOpt(TASK_ARG_LONG).required(false).desc("choose task path, default [" + SOURCE_PATH + "]").build());
        options.addOption(Option.builder(ARG_HELP_SHORT).longOpt(ARG_HELP_LONG).desc("show command help").build());
        return options;
    }

    public static void printHelp() {

        Options options = buildOptions();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("java -jar ${jar} [options]", options);

        System.out.println("HELP");
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

        if (commandLine.hasOption(ARG_HELP_LONG)) {
            printHelp();
            System.exit(0);
        }
        return commandLine;
    }


    public static void main(String[] args) {

        CommandLine commandLine = parseArguments(args);
        String scriptSource = commandLine.getOptionValue(TASK_ARG_LONG, SOURCE_PATH);
        System.out.println("script source: {}" + scriptSource);



//        ManagerCLI managerCLI = new ManagerCLI();
//        String license = managerCLI.generateLicense("YourProject", "BASIC", 36);
//
//        System.out.println("License of Current Machine:");
//        System.out.println(license);
    }
}
