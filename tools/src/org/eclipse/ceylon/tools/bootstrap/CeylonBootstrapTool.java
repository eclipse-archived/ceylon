package org.eclipse.ceylon.tools.bootstrap;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.CeylonBaseTool;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.launcher.Bootstrap;
import org.eclipse.ceylon.launcher.LauncherUtil;

@Summary("Generates a Ceylon bootstrap script in the current directory")
@Description("This tool generates a `ceylonb` bootstrap shell script "
        + "(and a `ceylonb.bat` batch file for Windows) that functions "
        + "exactly like the normal `ceylon` command, except that it is "
        + "not necessary to install Ceylon yourself. On first execution "
        + "the scripts will check if the required Ceylon distribution is "
        + "already available locally and if not they will download and "
        + "install it."
        + "\n\n"
        + "A bootstrap script can be used to make it very easy for "
        + "authors of Ceylon projects to distribute their code to users "
        + "without them having to install Ceylon. It is also useful for "
        + "making sure that users use the exact same version of Ceylon "
        + "the code was tested with without forcing them to install "
        + "that exact same version themselves."
        + "\n\n"
        + "The `distribution` argument determines which distribution "
        + "the bootstrap script will install. It can either be a version "
        + "string, which will then be combined with the URL to the official "
        + "Ceylon download site, or it can be a URL pointing directly to "
        + "the desired Ceylon distribution download. If this option is not "
        + "specified the current version will be used "
        + "(default: " + Versions.CEYLON_VERSION_NUMBER + ")")
public class CeylonBootstrapTool extends CeylonBaseTool {
    private URI distribution;
    private File installation;
    private String shaSum;
    private boolean force;
        
    private static final String FILE_CEYLON_SCRIPT = "ceylon";
    private static final String FILE_CEYLONB_SCRIPT = "ceylonb";
    
    public CeylonBootstrapTool() {
    }
    
    @Argument(argumentName="distribution", multiplicity="?")
    public void setDistribution(URI distribution) {
        this.distribution = distribution;
    }

    @OptionArgument(argumentName="path")
    @Description("Determines where the bootstrap script will install the distribution. " +
            "This can either be a path that starts with `~` or `${user.home}` to " + 
            "indicate a location in the user's home directory or it can start with " +
            "`${ceylon.user.dir}` to indicate a location in the user's Ceylon " +
            "directory. (default: ${ceylon.user.dir}/dists/)")
    public void setInstallation(File installation) {
        this.installation = installation;
    }

    @OptionArgument(argumentName = "sum")
    @Description("The SHA-256 sum to be included for the bootstrap script (default: no sum)")
    public void setShaSum(String shaSum) {
        this.shaSum = shaSum;
    }

    @Option
    @Description("Create the bootstrap files even if they already exist")
    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        if (!force) {
            File sourceDir = applyCwd(new File(Constants.DEFAULT_SOURCE_DIR));
            File configDir = applyCwd(new File(Constants.CEYLON_CONFIG_DIR));
            if (!sourceDir.isDirectory() && !configDir.isDirectory()) {
                throw new IllegalStateException(CeylonBootstrapMessages.msg("error.not.project"));
            }
            
            File scriptFile = applyCwd(new File(FILE_CEYLONB_SCRIPT));
            File batFile = applyCwd(new File(FILE_CEYLONB_SCRIPT + ".bat"));
            File bootstrapDir = new File(applyCwd(new File(Constants.CEYLON_CONFIG_DIR)), "bootstrap");
            File propsFile = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_PROPERTIES);
            File jarFile = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_JAR);
            if (scriptFile.exists() || batFile.exists() || propsFile.exists() || jarFile.exists()) {
                throw new IllegalStateException(CeylonBootstrapMessages.msg("error.bootstrap.exists"));
            }
        }
        
        if (installation != null && installation.isAbsolute()) {
            throw new IllegalArgumentException(CeylonBootstrapMessages.msg("error.path.not.absolute"));
        }
        
        if (shaSum != null && shaSum.length() != 64) {
            throw new IllegalArgumentException(CeylonBootstrapMessages.msg("error.shasum.length"));
        }
    }
    
    @Override
    public void run() throws Exception {
        setupBootstrap(validCwd().getAbsoluteFile(), distribution, installation, shaSum);
    }

    public static void setupBootstrap(File targetDir, URI distribution, File installation, String shaSum) throws Exception {
        File srcJar = new File(LauncherUtil.determineLibs(LauncherUtil.determineHome()), Bootstrap.FILE_BOOTSTRAP_JAR);
        File srcScripts = new File(LauncherUtil.determineHome(), Constants.CEYLON_BIN_DIR);
        setupBootstrap(targetDir, srcJar, srcScripts, distribution, installation, shaSum);
    }

    public static void setupBootstrap(File targetDir, File srcJar, File srcScripts, URI distribution, File installation, String shaSum) throws Exception {
        // Create the target "bootstrap" directory
        File bootstrapDir = new File(new File(targetDir, Constants.CEYLON_CONFIG_DIR), "bootstrap");
        FileUtil.mkdirs(bootstrapDir);
        
        // Create the "ceylon-bootstrap.properties" file
        Properties props = new Properties();
        props.setProperty(Bootstrap.KEY_DISTRIBUTION, getDistributionUri(distribution).toString());
        if (installation != null) {
            props.setProperty(Bootstrap.KEY_INSTALLATION, installation.getPath());
        }
        if (shaSum != null) {
            props.setProperty(Bootstrap.KEY_SHA256SUM, shaSum);
        }
        File propsFile = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_PROPERTIES);
        try (FileOutputStream out = new FileOutputStream(propsFile)) {
            props.store(out, CeylonBootstrapMessages.msg("info.generated.by"));
        }
        
        // Copy the "ceylon-bootstrap.jar"
        File destJar = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_JAR);
        Files.copy(srcJar.toPath(), destJar.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        
        // Copy the "ceylon" startup script to "./ceylonb"
        File srcScript = new File(srcScripts, FILE_CEYLON_SCRIPT);
        File destScript = new File(targetDir, FILE_CEYLONB_SCRIPT);
        Files.copy(srcScript.toPath(), destScript.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        destScript.setExecutable(true, false);
        
        // Copy the "ceylon.bat" startup script to "./ceylonb.bat"
        File srcBat = new File(srcScripts, FILE_CEYLON_SCRIPT + ".bat");
        File destBat = new File(targetDir, FILE_CEYLONB_SCRIPT + ".bat");
        Files.copy(srcBat.toPath(), destBat.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }
    
    private static URI getDistributionUri(URI distribution) throws URISyntaxException {
        if (distribution == null || distribution.getScheme() == null) {
            String version;
            if (distribution != null) {
                version = distribution.getPath();
            } else {
                version = Versions.CEYLON_VERSION_NUMBER;
            }
            return new URI(Bootstrap.CEYLON_DOWNLOAD_BASE_URL + version.replace('.', '_'));
        } else {
            return distribution;
        }
    }
}
