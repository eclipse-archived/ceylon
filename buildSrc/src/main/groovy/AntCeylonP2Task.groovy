import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

class AntCeylonP2Task extends AbstractAntCeylonTask {

    /** Matches {@code sysRep} parameter on Ant task.
     *
     */
    @InputDirectory
    File getSysRepo() {
        project.file(this.sysRepo)
    }

    void setSysRepo(def repoDir) {
        this.sysRepo = repoDir
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

    /** Matches {@code repositoryName} parameter on Ant task.
     *
     */
    @Input
    String repositoryName

    /** Matches {@code categoryPrefix} parameter on Ant task.
     *
     */
    @Input
    String categoryPrefix

    /** Matches {@code categories} parameter on Ant task.
     *
     */
    @InputFile
    File getCategoriesFile() {
        project.file(this.categoriesFile)
    }

    void setCategoriesFile(def catXml) {
        this.categoriesFile = catXml
    }

    void addModule( final String modName, final String modVer) {
        modules += [ name : modName, version : modVer ]
    }

    @Input
    String getModuleListing() {
        modules.toString()
    }


    protected void setupAndRunAntTask() {
        Map params = [
            sysRep : getSysRepo().absolutePath,
            out : getDestinationDir().absolutePath,
            repositoryName : getRepositoryName(),
            categoryPrefix : getCategoryPrefix(),
            categories : getCategoriesFile()
        ]
        runAntTask params,'p2', { List< Map<String,String> > m ->
            m.each { Map<String,String> entry ->
                module entry
            }
        }.curry(this.modules)
    }

    private Object sysRepo
    private Object destinationDir
    private Object categoriesFile
    private List< Map<String,String> > modules = []

}