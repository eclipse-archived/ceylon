doc "A package descriptor."
shared class Package(

        doc "The name of the package."
        PackageName name,

        doc "The visibility of the package."
        Boolean shared = false,

        doc "A description of the package."
        String? doc=null,

        doc "The license under which the package
             is distributed."
        URL? license=null) {

    //TODO implement

}