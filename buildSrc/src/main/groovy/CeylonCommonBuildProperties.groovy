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
            ext.cbp = propFileLoader.load file(
                "${rootProject.projectDir}/common-build.properties"
            ), [ basedir : rootProject.projectDir,
                 'sun.boot.class.path' : ''
            ]

            if(ext.cbp == null) {
                throw new GradleException ('ext.cbp is not defined. Did you load common-build.properties correctly?')
            }


            ext.requiresCBP = { String propName ->

                if(cbp."${propName}" == null) {
                    throw new GradleException ("${propName} is not defined in common-build.properties")
                }
            }
        }

    }
}
