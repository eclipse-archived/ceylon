import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction

class AntCeylonImportJarTask extends AbstractAntCeylonTask {

    /** Matches {@code force} parameter on Ant task.
     *
     */
    @Input
    boolean force = true

    /** Matches {@code module} parameter on Ant task.
     *
     */
    @Input
    String module

    /** Matches {@code descriptor} parameter on Ant task.
     *
     */
    @Optional
    File getDescriptor() {
        this.descriptor ? project.file(this.descriptor) : null
    }

    void setDescriptor(def desc) {
        this.descriptor = desc
    }

    /** Matches {@code jar} parameter on Ant task.
     *
     */
    @InputFile
    File getJarFile() {
        project.file(this.jar)
    }

    void setJarFile(def jarFile) {
        this.jar = jarFile
    }

    /** Matches {@code out} parameter on Ant task.
     *
     */
    @OutputDirectory
    File getDestinationDir() {
        project.file(this.destinationDir)
    }

    void setDestinationDir(def dest) {
        this.destinationDir = dest
    }

    @OutputFiles
    FileCollection outputFiles() {
        List<String> mod = getModule().split('/',2)
        String prefix = "${destinationDir}/${mod[0].replace('.','/')}/${mod[1]}"
        List<String> fileNames = [
            "${prefix}/${getJarFile().name}",
            "${prefix}/${getJarFile().name}.sha1"
        ]
        if(this.descriptor) {
            fileNames+= [
                "${prefix}/${getDescriptor().name}",
                "${prefix}/${getDescriptor()/name}.sha1"
            ]
        }
        project.files(fileNames)
    }

    protected void setupAndRunAntTask() {
        Map params = [
            module : getModule(),
            jar : getJarFile().absolutePath,
            out : getDestinationDir().absolutePath,
            force : getForce(),
        ]
        if(this.descriptor) {
            params+= [ descriptor : getDescriptor().absolutePath ]
        }
        runAntTask params,'import-jar'
    }

    private Object jar
    private Object destinationDir
    private Object descriptor

}