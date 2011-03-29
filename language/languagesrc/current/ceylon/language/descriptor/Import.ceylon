doc "Specifies an imported module (dependency) of a
     module."
shared class Import(

        doc "The name of this imported module."
        ModuleName name,

        doc "The version id of this imported module."
        ModuleVersion version=null,

        doc "Determines if this imported module is
             required by the module."
        Boolean optional=false,

        doc "Determines if this imported module is
             exported transitively to other modules
             that import the module."
        Boolean export=false) {

    //TODO finish

}