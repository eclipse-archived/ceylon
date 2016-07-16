import org.gradle.api.GradleException

class CeylonCommonBuildPropertiesExtension extends TreeMap<String,String> {
    CeylonCommonBuildPropertiesExtension( Map<String,String> loadedMap ) {
        super(loadedMap)
    }

    void requires( final String propName ) {
        if(!containsKey(propName)) {
            throw new GradleException ("${propName} is not defined in common-build.properties")
        }
    }

    def propertyMissing(final String propName) {
        super.get(propName)
    }

}
