/**
 * @author Schalk W. Cronjé
 */
class CeylonBuildUtil {
    static String relativeTo(final File thisPath,final File toThatPath) {
        toThatPath.toPath().relativize( thisPath.toPath() ).toFile().toString()
    }
}
