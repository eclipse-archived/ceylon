import groovy.util.slurpersupport.GPathResult
import org.gradle.api.GradleException
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.bundling.Jar
import org.gradle.util.GradleVersion
import org.eclipse.ceylon.model.loader.OsgiVersion

/**
 * @author Schalk W. Cronjé
 */
abstract class AbstractCeylonOsgiArchiveTaskExtension {

    abstract void configureManifestForTask()

    /** Set to true if javax.lang.model.* needs to be imported
     *
     */
    boolean importJavaxModel = false

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

    /** If module names matches any of these, then {@code resolution:=optional} will be added
     * to a {@code Require-Bundle} irrespective of whether the module is marked optioonal or not
     * @param moduleNames
     */
    void forceOptionalResolutionFor(String... moduleNames) {
        optionalResolution.addAll(moduleNames as List)
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

    String getRequireBundle() {
        List<String> optRes = this.optionalResolution
        moduleContent.dependencies.module.findAll { module ->
            !(excludedModuleNames.contains(module.@name.toString()))
        }.collect { module ->
            String depOsgiVersion;
            if (module.@name.toString().contains("ceylon")) {
                depOsgiVersion = OsgiVersion.fromCeylonVersion(module.@slot.toString());
            } else {
                depOsgiVersion = module.@slot;
            }
            String attr = "${module.@name};bundle-version=${depOsgiVersion}"
            if(module.@export == 'true') {
                attr+=";visibility:=reexport"
            }
            if(module.@optional== 'true' || optRes.contains(module.@name.toString()) ) {
                attr+=";resolution:=optional"
            }
            attr
        }.join(',')

    }

    protected AbstractCeylonOsgiArchiveTaskExtension(AbstractArchiveTask task) {
        this.archiveTask = task
    }

    protected Map<String,String> getManifestInstructions(
        final String osgiBundleVersion,
        final String osgiBundleSymbolicName,
        final String exportedBundleVersion,
        final Map<String,String> osgiDynamicImports = [:]
    ) {
        Map<String,String> instructions = [
            'Bundle-SymbolicName' : osgiBundleSymbolicName,
            'Bundle-Version' :  osgiBundleVersion,
            'Export-Package' : "!about.html, !licenses, !settings.xml, *;version=\"${exportedBundleVersion}\"",
            'Require-Bundle' : getRequireBundle()
        ] as LinkedHashMap<String, String>

        if(osgiDynamicImports.size()) {
            String packages = osgiDynamicImports.collect { k,v ->
                k + ';bundle-version=' + v
            }.join(',')
            instructions+= [ 'DynamicImport-Package' : packages ]
        }

        if(importJavaxModel) {
            instructions+= ['Import-Package' : 'javax.lang.model.*']
        } else {
            instructions+= ['-removeheaders' : 'Import-Package' ]
        }

        instructions+= [
            '-nouses' : 'true',
            'Gradle-Version' : GradleVersion.current().toString(),
            'DSTAMP' : TimeStamp.DSTAMP,
            'NOW' : TimeStamp.NOW,
            'TODAY' : TimeStamp.TODAY,
            'TSTAMP' : TimeStamp.TSTAMP
        ]

        instructions
    }

    protected AbstractArchiveTask getArchiveTask() {
        this.archiveTask
    }

    private def moduleLocation
    private List<String> optionalResolution = []
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

    private final AbstractArchiveTask archiveTask

}
