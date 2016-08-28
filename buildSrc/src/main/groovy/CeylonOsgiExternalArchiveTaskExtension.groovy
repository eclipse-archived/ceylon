import groovy.util.slurpersupport.GPathResult
import org.gradle.api.GradleException
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.util.GradleVersion

import java.util.jar.Attributes

class CeylonOsgiExternalArchiveTaskExtension  extends AbstractCeylonOsgiArchiveTaskExtension  {

    CeylonOsgiExternalArchiveTaskExtension( AbstractArchiveTask task ) {
        super(task)
    }

    CeylonOsgiExternalArchiveTaskExtension call( Closure cfg ) {
        def config = cfg.clone()
        config.delegate = this
        config()
        this
    }

    /** If set to {@code true}, new manifest will be created irrespective of whether
     * JAR contains a valid OSGI manifest.
     */
    boolean forceNewOsgiManifest = false

    /** An postfix to identify external dependencies which make up parts of the Ceylon distribution.
     *
     */
    String externalBundleQualifier = 'CEYLON-DEPENDENCIES-v0'

    String suggestNewBundleVersion(final String srcBundleVersion) {
        switch(srcBundleVersion) {
            case ~/^[0-9]+\.[0-9]+\.[0-9]+$/ :
                return "${srcBundleVersion}.${externalBundleQualifier}"
                break
            case ~/^[0-9]+\.[0-9]+$/ :
                return "${srcBundleVersion}.0.${externalBundleQualifier}"
                break
            case ~/^[0-9]+$/ :
                return "${srcBundleVersion}.0.0.${externalBundleQualifier}"
                break
            case ~/^[0-9]+\.[0-9]+\.[0-9]+\.[^.]+$/ :
                return "${srcBundleVersion}-${externalBundleQualifier}"
                break
            default:
                throw new GradleException("External dependency (${bundleSymbolicName}) with a non-supported version")
        }

    }

    void seedFrom(final File originalJar) {
        Map<String,String> attrs = [:]
        originalJar.withInputStream { input ->
            Attributes mainAttributes = new java.util.jar.JarInputStream(input).manifest.mainAttributes
            mainAttributes.keySet().each { k ->
                attrs[k.toString()] = mainAttributes.getValue(k).toString()
            }
        }
        this.seedAttributes.putAll(attrs)
    }

    @Override
    void configureManifestForTask() {
        GPathResult module = moduleContent
        String bundleVersion
        String newBundleVersion
        String bundleSymbolicName = module.@name
        boolean hasOsgi = false


        if(!seedAttributes.empty) {
            hasOsgi = seedAttributes['Bundle-Version']?.size()
            bundleVersion = seedAttributes['Bundle-Version'] ?: module.@slot
            newBundleVersion = suggestNewBundleVersion(bundleVersion)
        }


        if(!hasOsgi || forceNewOsgiManifest) {
            Map instructions = getManifestInstructions(
                newBundleVersion,
                bundleSymbolicName,
                bundleVersion ?: module.@slot
            )
            Map <String,String> merged = [:]
            merged.putAll(seedAttributes)
            merged.putAll(instructions.findAll{!it.key.startsWith('-')})
            merged.remove('Import-Package')

            archiveTask.manifest.attributes merged
            instructions.each { k,v ->
                if(k.startsWith('-')) {
                    archiveTask.manifest.instruction(k,v)
                }
            }
        } else {
            seedAttributes['Bundle-Version'] = newBundleVersion
            archiveTask.manifest.attributes seedAttributes
            archiveTask.manifest.instructionReplace 'Gradle-Version', GradleVersion.current().toString()
        }

    }

    private final Map<String,String> seedAttributes = [:]

}
