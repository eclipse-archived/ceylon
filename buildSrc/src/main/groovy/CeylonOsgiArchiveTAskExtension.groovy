import groovy.util.slurpersupport.GPathResult
import org.gradle.api.GradleException
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.util.GradleVersion

/** This extension will be added to each Archive task if
 * the {@code makeOsgiArchive()} extensions ies executed within the archive
 * configuration (the latter depends on it being added by the {@code CeylonOsgi}
 * plugin).
 */
class CeylonOsgiArchiveTaskExtension {

    CeylonOsgiArchiveTaskExtension( AbstractArchiveTask task ) {
        this.archiveTask = task
    }

    CeylonOsgiArchiveTaskExtension call( Closure cfg ) {
        def config = cfg.clone()
        config.delegate = this
        config()
        this
    }

    /** Set to true if javax.lang.model.* needs to be imported
     *
     */
    boolean importJavaxModel = false

    /** Set the symbolic name for this bundle.
     * Affects {@code Bundle-SymbolicName}
     */
    String bundleSymbolicName

    /** Sets the exported bundle version.
     * Affects {@code Export-Package}
     */
    String exportedBundleVersion

    /** Gets the bundle version
     *
     * @return If the bundle versions has not been set, return the exported bundle version
     */
    String getBundleVersion() {
        this.bundleVersion ?: exportedBundleVersion
    }

//    void bundleVersion(final String bv) {
//        this.bundleVersion = bv
//    }

    /** Sets an OSGI bundle version.
     * Affects {@code Bundle-Version}
     *
     * @param bv Version string
     */
    void setBundleVersion(final String bv) {
        this.bundleVersion = bv
    }

    /** Get list of excluded modules
     *
     * @return
     */
    List<String> getExcludedModuleNames() {
        this.excludedModuleNames
    }

    /** Add addition modules to be excluded
     *
     * @param names
     */
    void excludedModuleNames(String... names) {
        this.excludedModuleNames.addAll(names as List)
    }

    /** Override the existing list of excluded module names with a new list.
     *
     * @param newModuleNames
     */
    void setExcludedModuleNames(final List<String> newModuleNames) {
        this.excludedModuleNames.clear()
        this.excludedModuleNames.addAll(newModuleNames)
    }

    /** If module names matches any of these, then {@code resolution:=optional} will be added
     * to a {@code Require-Bundle} irrespective of whether the module is marked optioonal or not
     * @param moduleNames
     */
    void forceOptionalResolutionFor(String... moduleNames) {
        optionalResolution.addAll(moduleNames as List)
    }

    /** Add packages that needs to be dynamically imported.
     *
     * @param importMappings A map in the form of {@code PackagePattern : BundleVersion }
     */
    void dynamicImports( Map<String,String> importMappings ) {
        this.dynamicImports+= importMappings
    }

    /** Get the list of OSGI manifest instructions as a map
     *
     * @return
     */
    Map<String,String> getManifestInstructions() {
        Map<String,String> instructions = [
            'Bundle-Version' :  getBundleVersion(),
            'Bundle-SymbolicName' : this.bundleSymbolicName,
            'Export-Package' : "!about.html, !licenses, !settings.xml, *;version=\"${this.exportedBundleVersion}\"",
            '-nouses' : 'true',
            'Gradle-Version' : GradleVersion.current().toString(),
            'DSTAMP' : TimeStamp.DSTAMP,
            'NOW' : TimeStamp.NOW,
            'TODAY' : TimeStamp.TODAY,
            'TSTAMP' : TimeStamp.TSTAMP
        ]

        if(dynamicImports.size()) {
            String packages = dynamicImports.collect { k,v ->
                k + ';bundle-version=' + v
            }.join(',')
            instructions+= [ 'DynamicImport-Package' : packages ]
        }

        if(importJavaxModel) {
            instructions+= ['Import-Package' : 'javax.lang.model.*']
        } else {
            instructions+= ['-removeheaders' : 'Import-Package' ]
        }

        String requires = getRequireBundle()
        if(!requires?.empty) {
            instructions+= ['Require-Bundle' : requires]
        }
        instructions
    }

    /** Sets the location of the {@code module.xml} file in a lazy-evaluatable manner
     *
     * @param location Any location that can be resolve by Gradle's {@code project.file} method.
     *
     */
    void setModuleLocation(def location) {
        this.moduleLocation = location
    }

    /** Returns the location of the {@code module.xml} file
     *
     * @return
     */
    File getModuleLocation() {
        if(this.moduleLocation == null) {
            throw new GradleException('moduleLocation cannot be null')
        }
        archiveTask.project.file(this.moduleLocation)
    }

    /** Gets the {@code mpdule.xml} as traversable tree
     *
     * @return
     */
    GPathResult getModuleContent() {
        new XmlSlurper().parse(getModuleLocation())
    }

    /** Returns a string suitable for use as the value of {@code Require-Bundle}.
     *
     * @return
     */
    String getRequireBundle() {
        List<String> optRes = this.optionalResolution
        moduleContent.dependencies.module.findAll { module ->
            !(excludedModuleNames.contains(module.@name.toString()) /*||
                module.@name=='ceylon.language' && bundleSymbolicName == 'com.redhat.ceylon.model'*/)
        }.collect { module ->
            String attr = "${module.@name};bundle-version=${module.@slot}"
            if(module.@export == 'true') {
                attr+=";visibility:=reexport"
            }
            if(module.@optional== 'true' || optRes.contains(module.@name.toString()) ) {
                attr+=";resolution:=optional"
            }
            attr
        }.join(',')

    }

    private AbstractArchiveTask archiveTask
    private String bundleVersion
    private List<String> excludedModuleNames = [
        'java.base','java.compiler','java.logging',
        'javax.xml','java.instrument',
        'javax.jaxws','java.desktop',
        'java.prefs','oracle.jdk.base',
        'java.jdbc',
        'javax.script',
        'java.auth.kerberos',
        'java.tls',
        'java.management',
        'irg.sl4j.simple'
    ]
    private List<String> optionalResolution = []
    private Map<String,String> dynamicImports = [:]

    private def moduleLocation
}
