import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.apache.tools.ant.filters.ReplaceTokens

class CeylonBuildModuleXml extends DefaultTask {
    String version = project.version

    CeylonBuildModuleXml() {
        super()
        group = 'build'
        description = 'Creates a module.xml from a template file'
    }

    @InputFile
    File getSourceModule() {
        project.file(this.sourceModule)
    }

    void setSourceModule(Object f) {
        this.sourceModule = f
    }

    File getDestinationDir() {
        project.file(this.destinationDir)
    }

    void setDestinationDir(Object dest) {
        this.destinationDir = dest
    }

    @OutputFile
    File getDestinationFile() {
        if(this.destinationDir == null) {
            throw new GradleException('Destination directory has not been set')
        }
        new File(getDestinationDir(),'module.xml')
    }

    @TaskAction
    void exec() {
        project.copy {
            from getSourceModule()
            into getDestinationDir()
            rename '.+','module.xml'
            filter ReplaceTokens, tokens : [ 'ceylon-version' : this.version ]
        }
    }

    private Object sourceModule
    private Object destinationDir = { "${project.buildDir}/ceylon-module" }
}
