import org.gradle.api.GradleException

/**
 * @author Schalk W. Cronjé
 */
class CeylonCommonBuildPropertiesExtension extends TreeMap<String,String> {
    CeylonCommonBuildPropertiesExtension( Map<String,String> loadedMap ) {
        super(loadedMap)
//        props = loadedMap
    }

    void requires( final String propName ) {
        if(!containsKey(propName)) {
            throw new GradleException ("${propName} is not defined in common-build.properties")
        }
    }

    def propertyMissing(final String name) {
        super.get(name)
    }

//    def propertyMissing(final String name) {
//        props[name]
//    }
//
//    private TreeMap<String,String> props

}
