package com.redhat.ceylon.common;

public class Versions {
    
    // The current version is Ceylon 1.2.0 "A Series Of Unlikely Explanations"
    // This comment is here so this file will show up in searches for the current version number
    
    /****************************************************************************************************
    * WARNING Don't forget to update:
    * - ceylon.language/src/ceylon/language/module.ceylon
    * - ceylon.language/src/ceylon/language/language.ceylon
    * - ceylon.language/test/process.ceylon (versions, name, binary version)
    * - ceylon-dist/build.properties (versions)
    * - ceylon-dist/osgi/module.properties (versions)
    * - ceylon-dist/osgi/META-INF/MANIFEST.MF (versions)
    * - ceylon-dist/osgi/embeddedRepository/META-INF/MANIFEST.MF (versions)
    * - ceylon-dist/samples (versions, but not sure why we do that)
    * - ceylon-dist/README.MD (versions, name)
    * - ceylon-common/common-build.properties (version)
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

    public static final int CEYLON_VERSION_MAJOR = 1;
    public static final int CEYLON_VERSION_MINOR = 2;
    public static final int CEYLON_VERSION_RELEASE = 0;
    /**
     * The MAJOR.MINOR.RELEASE version.
     */
    public static final String CEYLON_VERSION_NUMBER = CEYLON_VERSION_MAJOR + "." + CEYLON_VERSION_MINOR + "." + CEYLON_VERSION_RELEASE;
    /**
     * The release code name.
     */
    public static final String CEYLON_VERSION_NAME = "A Series Of Unlikely Explanations";
    /**
     * The version number + code name description string.
     */
    public static final String CEYLON_VERSION = CEYLON_VERSION_NUMBER + " (" + CEYLON_VERSION_NAME + ")";

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
     */
    public static final int JVM_BINARY_MAJOR_VERSION = 8;
    public static final int JVM_BINARY_MINOR_VERSION = 0;
    public static final int JS_BINARY_MAJOR_VERSION = 8;
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

    // Dependencies that end up in code
    public static final String DEPENDENCY_JBOSS_MODULES_VERSION = "1.3.3.Final";
    public static final String DEPENDENCY_JANDEX_VERSION = "1.0.3.Final";
    
    /**
     * Is the given binary version compatible with the current version
     * @param major the binary version to check for compatibility
     * @param minor the binary version to check for compatibility
     * @return true if the current version of ceylon can consume the given binary version
     */
    public static boolean isJvmBinaryVersionSupported(int major, int minor){
        // latest version 1.1.1 supports 1.1 (bin 7.0) and 1.1.1 (bin 8.0)
        return (major == JVM_BINARY_MAJOR_VERSION
                || major == V1_1_BINARY_MAJOR_VERSION)
                && minor == JVM_BINARY_MINOR_VERSION;
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
        // latest version 1.2 supports 1.1 (bin 7.0) and 1.2 (bin 8.0)
        if(consumerMajor == V1_2_BINARY_MAJOR_VERSION && consumerMinor == V1_2_BINARY_MINOR_VERSION){
            return (major == V1_2_BINARY_MAJOR_VERSION 
                    || major == V1_1_BINARY_MAJOR_VERSION) && minor == 0;
        }
        // other versions must be equal
        return consumerMajor == major && consumerMinor == minor;
    }
}
