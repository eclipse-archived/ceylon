package com.redhat.ceylon.common;

public class Versions {
    
    // The current version is Ceylon 1.2.2 "In A Galaxy Far Far Away"
    // This comment is here so this file will show up in searches for the current version number
    
    /****************************************************************************************************
    * WARNING Don't forget to update:
    * - language/src/ceylon/language/module.ceylon
    * - language/src/ceylon/language/language.ceylon
    * - language/test/process.ceylon (versions, name, binary version)
    * - runtime/dist/dist-overrides.xml
    * - cmr/api/src/main/resources/com/redhat/ceylon/cmr/api/dist-overrides.xml
    * - dist/build.properties (versions)
    * - dist/osgi/module.properties (versions)
    * - dist/osgi/META-INF/MANIFEST.MF (versions)
    * - dist/osgi/embeddedRepository/META-INF/MANIFEST.MF (versions)
    * - README.md and dist/README.MD (versions, name, doc links)
    * - common-build.properties (version)
    * - ceylon-ide-eclipse/plugins/com.redhat.ceylon.eclipse.ui/about.ini (version, name)
    *   Find all files on IDE which need to be updated:
    *    grep -rl --exclude-dir 'target' --exclude-dir required-bundle-proxies --exclude-dir .git --exclude '*.class' --exclude '*.car' --exclude '*.idx' '1\.1\.0' .
    * - ceylon-compiler Eclipse build path (ceylon.language/ide-dist/ceylon.language-XXX.car)
    * - ceylon.language Eclipse build path (ceylon.language/ide-dist/ceylon.language-XXX.car)
    * 
    * If you bump the binary version:
    * - *.java,*.src @(\.?com.redhat.ceylon.compiler.java.metadata.)?Ceylon\(\s*major\s*=\s*6 -> @$1Version(major = 7
    *
    * If you bump the SDK:
    * - ceylon-sdk/source
    * - ceylon-sdk/source-js
    * - ceylon-sdk/test-source
    ****************************************************************************************************/

    /** 
     * The MAJOR part of the Ceylon version number (major.minor.release).
     * <em>Beware</em> javac will inline this at compile time at use-sites,
     * use {@link #getCeylonVersionMajor()} to avoid that.
     */
    public static final int CEYLON_VERSION_MAJOR = 1;
    /** 
     * The MINOR part of the Ceylon version number (major.minor.release).
     * <em>Beware</em> javac will inline this at compile time at use-sites,
     * use {@link #getCeylonVersionMinor()} to avoid that.
     */
    public static final int CEYLON_VERSION_MINOR = 2;
    /** 
     * The RELEASE part of the Ceylon version number (major.minor.release).
     * <em>Beware</em> javac will inline this at compile time at use-sites,
     * use {@link #getCeylonVersionRelease()} to avoid that.
     */
    public static final int CEYLON_VERSION_RELEASE = 2;
    
    // SHA1 of current HEAD at moment of compilation
    public static final String CURRENT_COMMIT_ID = "@commit@";
    
    /**
     * The MAJOR.MINOR.RELEASE version.
     */
    public static final String CEYLON_VERSION_NUMBER = CEYLON_VERSION_MAJOR + "." + CEYLON_VERSION_MINOR + "." + CEYLON_VERSION_RELEASE;
    
    /**
     * The release code name.
     */
    public static final String CEYLON_VERSION_NAME = "In A Galaxy Far Far Away";
    
    /**
     * The version number + code name description string.
     */
    public static final String CEYLON_VERSION = CEYLON_VERSION_NUMBER + " " + CURRENT_COMMIT_ID + " (" + CEYLON_VERSION_NAME + ")";

    /**
     * M1 and M2 are 0.0 since they were not tagged at the time
     * M3 is 1.0 as the first version with binary version information
     * M3.1 is 2.0
     * M4 is 3.0
     * M5 is 4.0
     * M6 is 5.0
     * 1.0 is 6.0
     * 1.1 is 7.0
     * 1.2.0 is 8.0
     * 1.2.1, 1.2.2 is 8.0 for JVM, 9.0 for JS
     */
    public static final int JVM_BINARY_MAJOR_VERSION = 8;
    public static final int JVM_BINARY_MINOR_VERSION = 0;
    public static final int JS_BINARY_MAJOR_VERSION = 9;
    public static final int JS_BINARY_MINOR_VERSION = 0;
    
    public static final int M1_BINARY_MAJOR_VERSION = 0;
    public static final int M1_BINARY_MINOR_VERSION = 0;
    
    public static final int M2_BINARY_MAJOR_VERSION = 0;
    public static final int M2_BINARY_MINOR_VERSION = 0;
    
    public static final int M3_BINARY_MAJOR_VERSION = 1;
    public static final int M3_BINARY_MINOR_VERSION = 0;
    
    public static final int M3_1_BINARY_MAJOR_VERSION = 2;
    public static final int M3_1_BINARY_MINOR_VERSION = 0;
    
    public static final int M4_BINARY_MAJOR_VERSION = 3;
    public static final int M4_BINARY_MINOR_VERSION = 0;
    
    public static final int M5_BINARY_MAJOR_VERSION = 4;
    public static final int M5_BINARY_MINOR_VERSION = 0;
    
    public static final int M6_BINARY_MAJOR_VERSION = 5;
    public static final int M6_BINARY_MINOR_VERSION = 0;
    
    public static final int V1_0_BINARY_MAJOR_VERSION = 6;
    public static final int V1_0_BINARY_MINOR_VERSION = 0;
    
    public static final int V1_1_BINARY_MAJOR_VERSION = 7;
    public static final int V1_1_BINARY_MINOR_VERSION = 0;
    
    public static final int V1_2_BINARY_MAJOR_VERSION = 8;
    public static final int V1_2_BINARY_MINOR_VERSION = 0;

    public static final int V1_2_1_JVM_BINARY_MAJOR_VERSION = 8;
    public static final int V1_2_1_JVM_BINARY_MINOR_VERSION = 0;

    public static final int V1_2_1_JS_BINARY_MAJOR_VERSION = 9;
    public static final int V1_2_1_JS_BINARY_MINOR_VERSION = 0;

    // Dependencies that end up in code
    public static final String DEPENDENCY_JBOSS_MODULES_VERSION = "1.4.4.Final";
    public static final String DEPENDENCY_JANDEX_VERSION = "2.0.0.Final";
    public static final String DEPENDENCY_LOGMANAGER_VERSION = "2.0.3.Final";

    public static final String DEPENDENCY_MAVEN_SUPPORT_VERSION = "2.0";
    
    /**
     * Is the given binary version compatible with the current version
     * @param major the binary version to check for compatibility
     * @param minor the binary version to check for compatibility
     * @return true if the current version of ceylon can consume the given binary version
     */
    public static boolean isJvmBinaryVersionSupported(int major, int minor){
        return major == JVM_BINARY_MAJOR_VERSION
                && minor == JVM_BINARY_MINOR_VERSION;
    }
    
    /**
     * Is the given binary version compatible with the current version
     * @param major the binary version to check for compatibility
     * @param minor the binary version to check for compatibility
     * @return true if the current version of ceylon can consume the given binary version
     */
    public static boolean isJsBinaryVersionSupported(int major, int minor){
        return major == JS_BINARY_MAJOR_VERSION
                && minor == JS_BINARY_MINOR_VERSION;
    }

    /**
     * Can the given consumer binary version consume modules compiled for the given binary version
     * @param consumerMajor the binary version that wants to consume the given binary version
     * @param consumerMinor the binary version that wants to consume the given binary version
     * @param major the binary version to check for compatibility
     * @param minor the binary version to check for compatibility
     * @return true if the given consumer version of ceylon can consume the given binary version
     */
    public static boolean isBinaryVersionCompatible(int consumerMajor, int consumerMinor, int major, int minor){
        return consumerMajor == major && consumerMinor == minor;
    }
    
    /**
     * Returns {@link #CEYLON_VERSION_MAJOR} 
     * (for those who don't want {@code javac} to inline it at compile time)
     */
    public static int getCeylonVersionMajor() {
        return CEYLON_VERSION_MAJOR;
    }
    
    /**
     * Returns {@link #CEYLON_VERSION_MINOR} 
     * (for those who don't want {@code javac} to inline it at compile time)
     */
    public static int getCeylonVersionMinor() {
        return CEYLON_VERSION_MINOR;
    }
    
    /**
     * Returns {@link #CEYLON_VERSION_RELEASE} 
     * (for those who don't want {@code javac} to inline it at compile time)
     */
    public static int getCeylonVersionRelease() {
        return CEYLON_VERSION_RELEASE;
    }
    
    /**
     * Returns {@link #CEYLON_VERSION_NAME} 
     * (for those who don't want {@code javac} to inline it at compile time)
     */
    public static String getCeylonVersionName() {
        return CEYLON_VERSION_NAME;
    }
    
    public static String getCeylonVersionNumber() {
        return CEYLON_VERSION_NUMBER;
    }
    
    public static String getCeylonVersion() {
        return CEYLON_VERSION;
    }
    
    /**
     * Returns the language module version associated with the given
     * binary major and minor numbers. If more than one version exists
     * it should give the lowest version (ie the first language module
     * that was compiled with that particular binary version).
     * For unknown or illegal binary versions it will return null.
     */
    public static String getJvmLanguageModuleVersion(int binaryMajor, int binaryMinor) {
        if (binaryMajor == 0 && binaryMinor == 0) {
            return "0.5"; // should be "0.2" but "0.5" is the lowest version available on the Herd
        } else if (binaryMajor == 1 && binaryMinor == 0) {
            return "0.5"; // should be "0.3"
        } else if (binaryMajor == 2 && binaryMinor == 0) {
            return "0.5"; // should be "0.3.1"
        } else if (binaryMajor == 3 && binaryMinor == 0) {
            return "0.5"; // should be "0.4"
        } else if (binaryMajor == 4 && binaryMinor == 0) {
            return "0.5";
        } else if (binaryMajor == 5 && binaryMinor == 0) {
            return "0.6";
        } else if (binaryMajor == 6 && binaryMinor == 0) {
            return "1.0.0";
        } else if (binaryMajor == 7 && binaryMinor == 0) {
            return "1.1.0";
        } else if (binaryMajor == 8 && binaryMinor == 0) {
            return "1.2.0";
        }
        return null;
    }
}
