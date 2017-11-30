import org.eclipse.ceylon.model.loader.OsgiVersion
import org.gradle.api.tasks.bundling.AbstractArchiveTask

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

    /** Sets the bundle version.
     * Affects {@code Bundle-Version} and {@code Export-Package}
     */
    String bundleVersion

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
            bundleVersion,
            bundleSymbolicName,
            bundleVersion,
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

    private Map<String,String> dynamicImports = [:]

}
