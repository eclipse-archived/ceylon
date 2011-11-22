import ceylon.language { desc = doc }

desc "A package descriptor."
shared class Package(

        desc "The name of the package."
        Quoted name,

        desc "The visibility of the package."
        Boolean shared = false,

        desc "A description of the package."
        String doc="") {

    //TODO implement

}