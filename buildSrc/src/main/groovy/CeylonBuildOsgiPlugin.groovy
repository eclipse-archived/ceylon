import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.jvm.tasks.Jar

/**
 * @author Schalk W. Cronjé
 */
class CeylonBuildOsgiPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'ceylon'

    static void addOsgiTaskExtension(AbstractArchiveTask archiveTask) {
        archiveTask.extensions.create(EXTENSION_NAME, CeylonOsgiArchiveTaskExtension, archiveTask)
        archiveTask.doFirst {
            CeylonBuildOsgiPlugin.configureManifest(archiveTask)
        }
    }

    static void addOsgiArchiveMethod(AbstractArchiveTask archiveTask) {
        archiveTask.ext.setAsOsgiArchive = {
            archiveTask.manifest = archiveTask.project.osgiManifest ()
            archiveTask.manifest.classesDir = archiveTask.project.sourceSets.main.output.classesDir
            archiveTask.manifest.classpath = archiveTask.project.configurations.getByName("runtime")
            addOsgiTaskExtension(archiveTask)
        }
    }

    static void configureManifest(Jar task) {
        CeylonOsgiArchiveTaskExtension metadata = task.extensions.getByName(EXTENSION_NAME)
        task.manifest.name = metadata.bundleSymbolicName
        task.manifest {
            metadata.manifestInstructions.each { k,v ->
                instructionReplace k,v
            }
        }
    }

    void apply(Project project) {

        project.with {
            apply plugin : 'osgi'

            tasks.withType(Jar) {  task ->
                if(task.name != 'jar') {
                    CeylonBuildOsgiPlugin.addOsgiArchiveMethod(task)
                } else {
                    CeylonBuildOsgiPlugin.addOsgiTaskExtension(task)
                }
            }
            tasks.whenTaskAdded { task ->
                if(task instanceof Jar) {
                    CeylonBuildOsgiPlugin.addOsgiArchiveMethod(task)
                }
            }

            tasks.create 'moduleXml', CeylonBuildModuleXml
        }
    }
}
