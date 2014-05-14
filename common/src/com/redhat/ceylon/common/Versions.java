package com.redhat.ceylon.common;

public class Versions {
    
    // The current version is Ceylon 1.0.1 "Unflatzburged"
    // This comment is here so this file will show up in searches for the current version number
    
    /****************************************************************************************************
    * WARNING Don't forget to update:
    * - ceylon.language/runtime-js/process.js (versions, name, binary version)
    * - ceylon-spec/src/com/redhat/ceylon/compiler/typechecker/TypeChecker.java (language module version)
    * - *.java,*.src @(\.?com.redhat.ceylon.compiler.java.metadata.)?Ceylon\(\s*major\s*=\s*6 -> @$1Version(major = 7
    * - ceylon-dist/build.properties (versions)
    * - ceylon-common/common-build.properties (version)
    * - ceylon-ide-eclipse/plugins/com.redhat.ceylon.eclipse.ui/about.ini (version, name)
    * - ceylon.language/test/process.ceylon (versions, name, binary version)
    * - ceylon-compiler Eclipse build path (ceylon.language/ide-dist/ceylon.language-XXX.car)
    * - ceylon.language Eclipse build path (ceylon.language/ide-dist/ceylon.language-XXX.car)
    ****************************************************************************************************/

    public static final int CEYLON_VERSION_MAJOR = 1;
    public static final int CEYLON_VERSION_MINOR = 1;
    public static final int CEYLON_VERSION_RELEASE = 0;
    /**
     * The MAJOR.MINOR.RELEASE version.
     */
    public static final String CEYLON_VERSION_NUMBER = CEYLON_VERSION_MAJOR + "." + CEYLON_VERSION_MINOR + "." + CEYLON_VERSION_RELEASE;
    /**
     * The release code name.
     */
    public static final String CEYLON_VERSION_NAME = "Unflatzburged";
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
     */
    public static final int JVM_BINARY_MAJOR_VERSION = 7;
    public static final int JVM_BINARY_MINOR_VERSION = 0;
    public static final int JS_BINARY_MAJOR_VERSION = 7;
    public static final int JS_BINARY_MINOR_VERSION = 0;

}
