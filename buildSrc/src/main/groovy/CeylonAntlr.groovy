import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

// This has been written because Gradle's Antlr plugin is broken. It is
// based upon an idea from Mike Meesson (https://discuss.gradle.org/t/gradle-2-7-2-8-causing-antlr-panic-cannot-find-importvocab-file/11709).
// It is not a fully flexible plugin, but it is sufficient for purposes of
// building Ceylon.
class CeylonAntlr implements Plugin<Project> {
    void apply(Project project) {
        project.with {
            apply plugin : 'java'
            configurations.maybeCreate 'antlr'
            CeylonAntlr.Generate genTask = tasks.create 'generateGrammarSource',CeylonAntlr.Generate
            genTask.group = 'Build'
            genTask.description = 'Generate source code from ANTLR grammar'
            tasks.getByName('compileJava').dependsOn genTask
        }
    }

    static class Generate extends DefaultTask {

        @OutputDirectory
        File getDestinationDirectory() {
            project.file(outputDir)
        }

        void setDestinationDirectory(Object dir) {
            outputDir = dir
        }

        void antlrGroup( final String relOutPath, Object src) {
            this.groups[relOutPath] = src
        }

        @Input
        Map<String,Set<String>> getAntlrGroups() {
            final String root = project.projectDir.absolutePath
            Map<String,Set<String>> ret = [:]
            this.groups.each { String relPath, def inputFiles ->
                ret[relPath] = project.files(inputFiles).files.collect { File f ->
                    CeylonBuildUtil.relativeTo(f,project.projectDir)
                } as Set
            }
            ret
        }

        @TaskAction
        void exec() {
            final String antlrClasspath = project.configurations.antlr.asPath
            final File root = destinationDirectory

            antlrGroups.each { String relPath, Set<String> inputFiles ->
                File out = new File(root,relPath)
                String outPath = CeylonBuildUtil.relativeTo(out,project.projectDir)
                out.mkdirs()
                project.javaexec {
                    main 'org.antlr.Tool'
                    classpath project.configurations.antlr
                    args '-fo',outPath
                    args inputFiles
                }
            }
        }

        private def outputDir = {project.file("${project.buildDir}/generated-source/java")}
        private Map<String,Object> groups = [:]

    }

}


