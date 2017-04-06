package com.redhat.ceylon.common;

public class Versions {
    
    // The current version is Ceylon "1.3.4-SNAPSHOT"@CEYLON_VERSION@ "Contents May Differ NEW"@CEYLON_VERSION_NAME@
    // This comment is here so this file will show up in searches for the current version number
    
    /****************************************************************************************************
    * WARNING Don't forget to update:
    * - language/src/ceylon/language/module.ceylon
    * - language/src/ceylon/language/language.ceylon
    * - language/test/process.ceylon (versions, name, binary version)
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
    public static final int CEYLON_VERSION_MAJOR = 1/*@CEYLON_VERSION_MAJOR@*/;
    /** 
     * The MINOR part of the Ceylon version number (major.minor.release).
     * <em>Beware</em> javac will inline this at compile time at use-sites,
     * use {@link #getCeylonVersionMinor()} to avoid that.
     */
    public static final int CEYLON_VERSION_MINOR = 3/*@CEYLON_VERSION_MINOR@*/;
    /** 
     * The RELEASE part of the Ceylon version number (major.minor.release).
     * <em>Beware</em> javac will inline this at compile time at use-sites,
     * use {@link #getCeylonVersionRelease()} to avoid that.
     */
    public static final int CEYLON_VERSION_RELEASE = 4/*@CEYLON_VERSION_RELEASE@*/;
    
    public static final String CEYLON_VERSION_QUALIFIER = "SNAPSHOT"/*@CEYLON_VERSION_QUALIFIER@*/;
    private static final String _CEYLON_VERSION_QUALIFIER = "-SNAPSHOT"/*@CEYLON_VERSION_PREFIXED_QUALIFIER@*/;
    
    // SHA1 of current HEAD at moment of compilation
    public static final String CURRENT_COMMIT_ID = "@commit@";
    
    /**
     * The MAJOR.MINOR.RELEASE version.
     */
    public static final String CEYLON_VERSION_NUMBER = CEYLON_VERSION_MAJOR + "." + CEYLON_VERSION_MINOR + "." + CEYLON_VERSION_RELEASE + _CEYLON_VERSION_QUALIFIER;
    
    /**
     * The release code name.
     */
    public static final String CEYLON_VERSION_NAME = "Contents May Differ NEW"/*@CEYLON_VERSION_NAME@*/;
    
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
     * 1.3.0 up to 1.3.3-SNAPSHOT is 8.1 for JVM
     * 1.3.0 up to 1.3.1 is 9.1 for JS
     * 1.3.3-SNAPSHOT is 10.0 for JS (support for sequenced annotations in the model)
     */
    public static final int JVM_BINARY_MAJOR_VERSION = 8;
    public static final int JVM_BINARY_MINOR_VERSION = 1;
    public static final int JS_BINARY_MAJOR_VERSION = 10;
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

    public static final int V1_2_2_JVM_BINARY_MAJOR_VERSION = 8;
    public static final int V1_2_2_JVM_BINARY_MINOR_VERSION = 0;

    public static final int V1_2_2_JS_BINARY_MAJOR_VERSION = 9;
    public static final int V1_2_2_JS_BINARY_MINOR_VERSION = 0;

    public static final int V1_3_0_JVM_BINARY_MAJOR_VERSION = 8;
    public static final int V1_3_0_JVM_BINARY_MINOR_VERSION = 1;

    public static final int V1_3_0_JS_BINARY_MAJOR_VERSION = 9;
    public static final int V1_3_0_JS_BINARY_MINOR_VERSION = 1;

    public static final int V1_3_1_JVM_BINARY_MAJOR_VERSION = 8;
    public static final int V1_3_1_JVM_BINARY_MINOR_VERSION = 1;

    public static final int V1_3_1_JS_BINARY_MAJOR_VERSION = 9;
    public static final int V1_3_1_JS_BINARY_MINOR_VERSION = 1;

    public static final int V1_3_2_JVM_BINARY_MAJOR_VERSION = 8;
    public static final int V1_3_2_JVM_BINARY_MINOR_VERSION = 1;

    public static final int V1_3_2_JS_BINARY_MAJOR_VERSION = 10;
    public static final int V1_3_2_JS_BINARY_MINOR_VERSION = 0;

    public static final int V1_3_3_JVM_BINARY_MAJOR_VERSION = 8;
    public static final int V1_3_3_JVM_BINARY_MINOR_VERSION = 1;

    public static final int V1_3_3_JS_BINARY_MAJOR_VERSION = 10;
    public static final int V1_3_3_JS_BINARY_MINOR_VERSION = 0;

    /*@NEW_VERSION_BINARY@*/
    
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
        return major == JVM_BINARY_MAJOR_VERSION;
    }
    
    /**
     * Is the given binary version compatible with the current version
     * @param major the binary version to check for compatibility
     * @param minor the binary version to check for compatibility
     * @return true if the current version of ceylon can consume the given binary version
     */
    public static boolean isJsBinaryVersionSupported(int major, int minor){
        return major == JS_BINARY_MAJOR_VERSION || major == V1_3_0_JS_BINARY_MAJOR_VERSION;
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
        return consumerMajor == major;
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
    
    public static class VersionDetails {
        public final String version;
        public final int binaryMajor;
        public final int binaryMinor;
        
        public VersionDetails(String version,
                int binaryMajor, int binaryMinor) {
            this.version = version;
            this.binaryMajor = binaryMajor;
            this.binaryMinor = binaryMinor;
        }
    }
    
    public static VersionDetails getDetailsByVersion(VersionDetails[] versions, String version) {
        for (VersionDetails vd : versions) {
            if (vd.version.equals(version)) {
                return vd;
            }
        }
        return null;
    }
    
    public static VersionDetails oldestCompatibleVersion(VersionDetails[] versions, VersionDetails version) {
        VersionDetails result = null;
        for (int i = versions.length - 1; i >= 0; i--) {
            VersionDetails vd = versions[i];
            if (vd.binaryMajor == version.binaryMajor) {
                result = vd;
            }
        }
        return result;
    }
    
    public static VersionDetails newestCompatibleVersion(VersionDetails[] versions, VersionDetails version) {
        VersionDetails result = null;
        for (VersionDetails vd : versions) {
            if (vd.binaryMajor == version.binaryMajor) {
                result = vd;
            }
        }
        return result;
    }
    
    public static final VersionDetails[] jvmVersions = {
            new VersionDetails("0.1",   M1_BINARY_MAJOR_VERSION, M1_BINARY_MINOR_VERSION),
            new VersionDetails("0.2",   M2_BINARY_MAJOR_VERSION, M2_BINARY_MINOR_VERSION),
            new VersionDetails("0.3",   M3_BINARY_MAJOR_VERSION, M3_BINARY_MINOR_VERSION),
            new VersionDetails("0.3.1", M3_1_BINARY_MAJOR_VERSION, M3_1_BINARY_MINOR_VERSION),
            new VersionDetails("0.4",   M4_BINARY_MAJOR_VERSION, M4_BINARY_MINOR_VERSION),
            new VersionDetails("0.5",   M5_BINARY_MAJOR_VERSION, M5_BINARY_MINOR_VERSION),
            new VersionDetails("0.6",   M6_BINARY_MAJOR_VERSION, M6_BINARY_MINOR_VERSION),
            new VersionDetails("1.0.0", V1_0_BINARY_MAJOR_VERSION, V1_0_BINARY_MINOR_VERSION),
            new VersionDetails("1.1.0", V1_1_BINARY_MAJOR_VERSION, V1_1_BINARY_MINOR_VERSION),
            new VersionDetails("1.2.0", V1_2_BINARY_MAJOR_VERSION, V1_2_BINARY_MINOR_VERSION),
            new VersionDetails("1.2.1", V1_2_1_JVM_BINARY_MAJOR_VERSION, V1_2_1_JVM_BINARY_MINOR_VERSION),
            new VersionDetails("1.2.2", V1_2_2_JVM_BINARY_MAJOR_VERSION, V1_2_2_JVM_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.0", V1_3_0_JVM_BINARY_MAJOR_VERSION, V1_3_0_JVM_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.1", V1_3_1_JVM_BINARY_MAJOR_VERSION, V1_3_1_JVM_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.2", V1_3_2_JVM_BINARY_MAJOR_VERSION, V1_3_2_JVM_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.3", V1_3_3_JVM_BINARY_MAJOR_VERSION, V1_3_3_JVM_BINARY_MINOR_VERSION),
            /*@NEW_VERSION_JVM_VERSIONS@*/
            new VersionDetails(CEYLON_VERSION_NUMBER, JVM_BINARY_MAJOR_VERSION, JVM_BINARY_MINOR_VERSION),
    };
    
    public static final VersionDetails[] jsVersions = {
            new VersionDetails("0.1",   M1_BINARY_MAJOR_VERSION, M1_BINARY_MINOR_VERSION),
            new VersionDetails("0.2",   M2_BINARY_MAJOR_VERSION, M2_BINARY_MINOR_VERSION),
            new VersionDetails("0.3",   M3_BINARY_MAJOR_VERSION, M3_BINARY_MINOR_VERSION),
            new VersionDetails("0.3.1", M3_1_BINARY_MAJOR_VERSION, M3_1_BINARY_MINOR_VERSION),
            new VersionDetails("0.4",   M4_BINARY_MAJOR_VERSION, M4_BINARY_MINOR_VERSION),
            new VersionDetails("0.5",   M5_BINARY_MAJOR_VERSION, M5_BINARY_MINOR_VERSION),
            new VersionDetails("0.6",   M6_BINARY_MAJOR_VERSION, M6_BINARY_MINOR_VERSION),
            new VersionDetails("1.0.0", V1_0_BINARY_MAJOR_VERSION, V1_0_BINARY_MINOR_VERSION),
            new VersionDetails("1.1.0", V1_1_BINARY_MAJOR_VERSION, V1_1_BINARY_MINOR_VERSION),
            new VersionDetails("1.2.0", V1_2_BINARY_MAJOR_VERSION, V1_2_BINARY_MINOR_VERSION),
            new VersionDetails("1.2.1", V1_2_1_JS_BINARY_MAJOR_VERSION, V1_2_1_JS_BINARY_MINOR_VERSION),
            new VersionDetails("1.2.2", V1_2_2_JS_BINARY_MAJOR_VERSION, V1_2_2_JS_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.0", V1_3_0_JS_BINARY_MAJOR_VERSION, V1_3_0_JS_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.1", V1_3_1_JS_BINARY_MAJOR_VERSION, V1_3_1_JS_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.2", V1_3_2_JS_BINARY_MAJOR_VERSION, V1_3_2_JS_BINARY_MINOR_VERSION),
            new VersionDetails("1.3.3", V1_3_3_JS_BINARY_MAJOR_VERSION, V1_3_3_JS_BINARY_MINOR_VERSION),
            /*@NEW_VERSION_JS_VERSIONS@*/
            new VersionDetails(CEYLON_VERSION_NUMBER, JS_BINARY_MAJOR_VERSION, JS_BINARY_MINOR_VERSION),
    };
    
    /**
     * Returns the language module version associated with the given
     * binary major and minor numbers. If more than one version exists
     * it should give the lowest version (ie the first language module
     * that was compiled with that particular binary version).
     * For unknown or illegal binary versions it will return null.
     */
    public static String getJvmLanguageModuleVersion(int binaryMajor, int binaryMinor) {
        if (binaryMajor < 4 && binaryMinor == 0) {
            // For very old versions we return "0.5" because
            // it is the lowest version available on the Herd
            return "0.5";
        }
        for (VersionDetails vd : jvmVersions) {
            if (vd.binaryMajor == binaryMajor && vd.binaryMinor == binaryMinor) {
                return vd.version;
            }
        }
        return null;
    }
}
