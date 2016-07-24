import org.gradle.api.Plugin
import org.gradle.api.Project

class CeylonBuildInfoPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'ceylonBuildInfo'
    void apply(Project project) {
        project.extensions.create( EXTENSION_NAME, CeylonBuildInfoPlugin.BuildInfo,project )
    }

    static class BuildInfo {
        final boolean hasGitRepository
        final String providedBuildId
        final Project project

        BuildInfo(Project project) {
            hasGitRepository = project.file("${project.rootProject.projectDir}/.git").exists()
            providedBuildId = project.properties.buildid?.trim() ?: System.getProperty('buildid')?.trim()
            this.project = project

            if(!hasGitRepository && providedBuildId == null) {
                project.logger.error "Git repository not found and -Pbuildid / -Dbuilid was not specified."
            }
        }

        String getRevisionInfo() {
            if (hasGitRepository) {
                OutputStream os = new ByteArrayOutputStream()
                project.exec {
                    standardOutput = os
                    commandLine 'git','rev-parse', '--short', 'HEAD'
                }
                os.toString().trim()
            } else  {
                providedBuildId
            }
        }

    }
}
