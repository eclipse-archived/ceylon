import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Schalk W. Cronjé
 */
class CeylonCommonBuildProperties implements Plugin<Project> {
    void apply(Project project) {
        project.with {
            apply plugin : 'com.admc.javaPropFile'

            // Everything in common-build.properties will be availabe on the 'cbp'
            // property object
            project.extensions.create 'cbp',  CeylonCommonBuildPropertiesExtension,
                propFileLoader.load (file(
                    "${rootProject.projectDir}/common-build.properties"
                ), [ basedir : rootProject.projectDir,
                     'sun.boot.class.path' : ''
                ])

            ext.requiresCBP = { String propName -> project.extensions.cbp.requires(propName) }
        }

    }

}
