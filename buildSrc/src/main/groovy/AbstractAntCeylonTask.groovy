import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction

abstract class AbstractAntCeylonTask extends DefaultTask {

    boolean fork = true

    void classpath(Object... cp) {
        antClasspath.addAll( cp as List )
    }

    protected String loadAntTask() {
        this.ant.typedef(
            format : 'xml',
            resource : 'org/eclipse/ceylon/ant/antlib.xml',
            classpath : project.files(antClasspath).asPath
        )
    }

    protected runAntTask( final Map props = [:], final String ceylonTaskName ) {
        this.ant."ceylon-${ceylonTaskName}" (props + [ fork : fork ])
    }

    protected runAntTask( final Map props = [:], final String ceylonTaskName, Closure cfg ) {
        this.ant."ceylon-${ceylonTaskName}" (props + [ fork : fork ],cfg)
    }

    abstract protected void setupAndRunAntTask()

    @TaskAction
    void exec() {
        loadAntTask()
        setupAndRunAntTask()
    }

    private List<Object> antClasspath = []

}
