package com.redhat.ceylon.tools.bootstrap;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.ResourceBundle;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.launcher.Bootstrap;
import com.redhat.ceylon.launcher.LauncherUtil;

@Summary("Generates a Ceylon bootstrap script in the current directory")
@Description("Generates a WAR file from the `.car` file of the "
        + "given `module-with-version`, "
        + "suitable for deploying to a standard Servlet container.\n\n"
        + "The version number is required since, in general, there "
        + "can be multiple versions available in the configured repositories.\n\n"
        + "The given module's `.car` file and those of its "
        + "transitive dependencies will be copied to the `WEB-INF/lib` of "
        + "the generated WAR file. Dependencies which are provided by "
        + "the application container "
        + "(and thus not required to be in `WEB-INF/lib`) can be "
        + "excluded using `--exclude-module`.")
public class CeylonBootstrapTool extends CeylonBaseTool {
    private URI distribution;
    private File installation;
    private String shaSum;
    private boolean force;
    
    private ResourceBundle bundle;
    
    private static final String CEYLON_DOWNLOAD_BASE_URL = "https://downloads.ceylon-lang.org/cli/";
    
    private static final String FILE_CEYLON_SCRIPT = "ceylon";
    private static final String FILE_CEYLONB_SCRIPT = "ceylonb";
    
    public CeylonBootstrapTool() {
        bundle = CeylonBootstrapMessages.RESOURCE_BUNDLE;
    }
    
    @OptionArgument(argumentName="version-or-url")
    @Description("Determines which distribution the bootstrap script will install. " +
            "Can either be a version string, which will then be combined with " +
            "the URL to the official Ceylon download site, or it can be a URL " +
            "pointing directly to the desired Ceylon distribution download. " +
            "If this option is not specified the current version will be used " +
            "(default: " + Versions.CEYLON_VERSION_NUMBER + ")")
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
        File sourceDir = applyCwd(new File(Constants.DEFAULT_SOURCE_DIR));
        File configDir = applyCwd(new File(Constants.CEYLON_CONFIG_DIR));
        if (!sourceDir.isDirectory() && !configDir.isDirectory()) {
            throw new IllegalStateException("The current directory doesn't seem to contain a Ceylon project");
        }
        
        if (!force) {
            File scriptFile = applyCwd(new File(FILE_CEYLONB_SCRIPT));
            File batFile = applyCwd(new File(FILE_CEYLONB_SCRIPT + ".bat"));
            File bootstrapDir = new File(applyCwd(new File(Constants.CEYLON_CONFIG_DIR)), "bootstrap");
            File propsFile = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_PROPERTIES);
            File jarFile = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_JAR);
            if (scriptFile.exists() || batFile.exists() || propsFile.exists() || jarFile.exists()) {
                throw new IllegalStateException("A bootstrap already exists in this directory, use the `--force` option to overwrite");
            }
        }
        
        if (installation != null && installation.isAbsolute()) {
            throw new IllegalArgumentException("The installation path cannot be absolute");
        }
        
        if (shaSum != null && shaSum.length() != 64) {
            throw new IllegalArgumentException("SHA sum does not have the correct length (64 characters)");
        }
    }
    
    @Override
    public void run() throws Exception {
        // Create the target "bootstrap" directory
        File bootstrapDir = new File(applyCwd(new File(Constants.CEYLON_CONFIG_DIR)), "bootstrap");
        FileUtil.mkdirs(bootstrapDir);
        
        // Create the "ceylon-bootstrap.properties" file
        Properties props = new Properties();
        props.setProperty(Bootstrap.KEY_DISTRIBUTION, getDistributionUri().toString());
        if (installation != null) {
            props.setProperty(Bootstrap.KEY_INSTALLATION, installation.getPath());
        }
        if (shaSum != null) {
            props.setProperty(Bootstrap.KEY_SHA256SUM, shaSum);
        }
        File propsFile = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_PROPERTIES);
        try (FileOutputStream out = new FileOutputStream(propsFile)) {
            props.store(out, "Generated by 'ceylon bootstrap'");
        }
        
        // Copy the "ceylon-bootstrap.jar"
        File srcJar = new File(LauncherUtil.determineLibs(LauncherUtil.determineHome()), Bootstrap.FILE_BOOTSTRAP_JAR);
        File destJar = new File(bootstrapDir, Bootstrap.FILE_BOOTSTRAP_JAR);
        Files.copy(srcJar.toPath(), destJar.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        
        // Copy the "ceylon" startup script to "./ceylonb"
        File srcScript = new File(new File(LauncherUtil.determineHome(), Constants.CEYLON_BIN_DIR), FILE_CEYLON_SCRIPT);
        File destScript = applyCwd(new File(FILE_CEYLONB_SCRIPT));
        Files.copy(srcScript.toPath(), destScript.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        
        // Copy the "ceylon.bat" startup script to "./ceylonb.bat"
        File srcBat = new File(new File(LauncherUtil.determineHome(), Constants.CEYLON_BIN_DIR), FILE_CEYLON_SCRIPT + ".bat");
        File destBat = applyCwd(new File(FILE_CEYLONB_SCRIPT + ".bat"));
        Files.copy(srcBat.toPath(), destBat.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }

    private URI getDistributionUri() throws URISyntaxException {
        if (distribution == null || distribution.getScheme() == null) {
            String version;
            if (distribution != null) {
                version = distribution.getPath();
            } else {
                version = Versions.CEYLON_VERSION_NUMBER;
            }
            return new URI(CEYLON_DOWNLOAD_BASE_URL + "ceylon-" + version + ".zip");
        } else {
            return distribution;
        }
    }
}
