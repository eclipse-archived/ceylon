import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction

class AntCeylonCompilerTask extends DefaultTask {

    void classpath(Object... cp) {
        antClasspath+= project.files(cp)
    }

    @TaskAction
    void exec() {

        ant.taskdef(
            name: "ceyloncompile${name.capitalize()}",
            classname: 'com.redhat.ceylon.ant.CeylonCompileAntTask',
            classpath: antClasspath.asPath()
        )

        ant."ceyloncompile${name.capitalize()}" (

        )
    }

    private FileCollection antClasspath

}