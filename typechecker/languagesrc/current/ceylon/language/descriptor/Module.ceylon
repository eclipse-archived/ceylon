import ceylon.language { desc = doc }

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
        String[] authors = {},

        desc "The license under which the module
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

        desc "Modules used by this module."
        Import... dependencies) {

    //TODO finish

}