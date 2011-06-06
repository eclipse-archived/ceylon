doc "A module descriptor."
shared class Module(

        doc "The name of the module."
        Quoted name,

        doc "The version id of the module."
        Quoted version,

        doc "A description of the module."
        String doc = "",
        
        doc "The names of the authors of the
             module"
        String[] authors = {},

        doc "The license under which the module
             is distributed."
        Quoted? license = null,

        /*doc "A method that is responsible for
             initializing state needed by the
             module. Called immediately after
             the module is loaded."
        void onLoad() = noop,

        doc "The entry point of a runnable
             module."
        void run() = notRunnable,*/

        doc "Modules used by this module."
        Import... dependencies) {

    //TODO finish

}