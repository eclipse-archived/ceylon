import groovy.util.slurpersupport.GPathResult
import org.gradle.api.GradleException
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.bundling.Jar
import org.gradle.util.GradleVersion

/** This extension will be added to each Archive task if
 * the {@code makeOsgiArchive()} extensions ies executed within the archive
 * configuration (the latter depends on it being added by the {@code CeylonOsgi}
 * plugin).
 */
class CeylonOsgiArchiveTaskExtension extends AbstractCeylonOsgiArchiveTaskExtension {

    CeylonOsgiArchiveTaskExtension( AbstractArchiveTask task ) {
        super(task)
    }

    CeylonOsgiArchiveTaskExtension call( Closure cfg ) {
        def config = cfg.clone()
        config.delegate = this
        config()
        this
    }

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

    /** Sets an OSGI bundle version.
     * Affects {@code Bundle-Version}
     *
     * @param bv Version string
     */
    void setBundleVersion(final String bv) {
        this.bundleVersion = bv
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
        getManifestInstructions(
            getBundleVersion(),
            getBundleSymbolicName(),
            getExportedBundleVersion(),
            dynamicImports
        )
    }

    @Override
    void configureManifestForTask() {
        archiveTask.manifest.name = bundleSymbolicName
        archiveTask.manifest {
            manifestInstructions.each { k,v ->
                instructionReplace k,v
            }
        }
    }

    private String bundleVersion
    private Map<String,String> dynamicImports = [:]

}
