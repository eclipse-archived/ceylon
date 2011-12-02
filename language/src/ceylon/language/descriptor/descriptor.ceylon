import ceylon.language { desc = doc }

desc "A package descriptor."
shared class Package(

        desc "The name of the package."
        Quoted name,

        desc "The visibility of the package."
        Boolean shared = false,

        desc "A description of the package."
        String doc="",

        desc "The names of the authors of the
             package"
        String[] by = {}) {

    //TODO implement

}

desc "Specifies an imported module (dependency) of a
     module."
shared class Import(

        desc "The name of this imported module."
        Quoted name,

        desc "The version id of this imported module."
        Quoted version,

        desc "Determines if this imported module is
             required by the module."
        Boolean optional=false,

        desc "Determines if this imported module is
             exported transitively to other modules
             that import the module."
        Boolean export=false) {

    //TODO finish

}

desc "A module descriptor."
shared class Module(

        desc "The name of the module."
        Quoted name,

        desc "The version id of the module."
        Quoted version,

        desc "A description of the module."
        String doc = "",
        
        desc "The names of the authors of the
             module"
        String[] by = {},

        desc "The license under which the module
             is distributed."
        Quoted? license = null,

        /*desc "A method that is responsible for
             initializing state needed by the
             module. Called immediately after
             the module is loaded."
        void onLoad() = noop,

        desc "The entry point of a runnable
             module."
        void run() = notRunnable,*/

        desc "Modules used by this module."
        Import... dependencies) {

    //TODO finish

}

