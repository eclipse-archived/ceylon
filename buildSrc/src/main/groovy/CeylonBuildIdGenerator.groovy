import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CeylonBuildIdGenerator extends DefaultTask {

    CeylonBuildIdGenerator() {
        this.buildInfo = project.extensions.getByType(CeylonBuildInfoPlugin.BuildInfo)
        enabled = this.buildInfo.hasGitRepository || this.buildInfo.providedBuildId != null


        outputs.upToDateWhen {
            if(this.outputFile !=null) {
                File out = getOutputFile()
                out.exists() && project.extensions.getByType(CeylonBuildInfoPlugin.BuildInfo).revisionInfo == out.text
            } else {
                false
            }
        }
    }

    /** Returns the output file
     *
     * @return
     */
    @OutputFile
    File getOutputFile() {
        project.file(this.outputFile)
    }

    /** Sets the output file
     *
     * @param f Anything that be converted to {@code project.file}
     */
    void setOutputFile(Object f) {
        this.outputFile = f
    }

    @TaskAction
    void exec() {
        String buildid = buildInfo.revisionInfo

        if(buildid) {
            getOutputFile().text = buildid
        }
    }

    private final CeylonBuildInfoPlugin.BuildInfo buildInfo
    private def outputFile
}
