import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.AbstractArchiveTask

class Checksum extends DefaultTask {

    String algorithm = 'sha1'

    void files(FileCollection fc) {
        collections+= fc
    }

    void archive(AbstractArchiveTask task) {
        archiveTasks+= task
    }

    @InputFiles
    FileCollection getArtifacts() {
        project.files(archiveTasks.collect {it.outputs.files}) + project.files(collections)
    }

    @OutputFiles
    Set<File> getOutputFiles() {
        artifacts.files.collect { File f ->
            new File("${f}.${algorithm}")
        } as Set<File>
    }

    @TaskAction
    void exec() {
        artifacts.files.each { File f ->
            ant.checksum file : f.absolutePath, algorithm : algorithm
        }
    }

    private List<AbstractArchiveTask> archiveTasks = []
    private List<FileCollection> collections = []
}
