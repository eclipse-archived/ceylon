import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.jvm.tasks.Jar

/**
 * @author Schalk W. Cronjé
 */
class CeylonBuildOsgiPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'ceylon'

    static void addOsgiTaskExtension(AbstractArchiveTask archiveTask,Class extType) {
        def ceylon = archiveTask.extensions.create(EXTENSION_NAME, extType, archiveTask)
        archiveTask.doFirst {
            ceylon.configureManifestForTask()
        }
    }

    static void addOsgiArchiveMethod(AbstractArchiveTask archiveTask) {
        archiveTask.ext.setAsOsgiArchive = {
            archiveTask.manifest = archiveTask.project.osgiManifest ()
            archiveTask.manifest.classesDir = archiveTask.project.sourceSets.main.output.classesDir
            archiveTask.manifest.classpath = archiveTask.project.configurations.getByName("runtime")
            addOsgiTaskExtension(archiveTask,CeylonOsgiArchiveTaskExtension)
        }
        archiveTask.ext.setAsOsgiExternalArchive = {
            archiveTask.manifest = archiveTask.project.osgiManifest ()
            archiveTask.manifest.classpath = archiveTask.project.fileTree('.') {exclude '**'}
            addOsgiTaskExtension(archiveTask,CeylonOsgiExternalArchiveTaskExtension)
        }
    }

    void apply(Project project) {

        project.with {
            apply plugin : 'osgi'

            tasks.withType(Jar) {  task ->
                if(task.name != 'jar') {
                    CeylonBuildOsgiPlugin.addOsgiArchiveMethod(task)
                } else {
                    CeylonBuildOsgiPlugin.addOsgiTaskExtension(task,CeylonOsgiArchiveTaskExtension)
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
