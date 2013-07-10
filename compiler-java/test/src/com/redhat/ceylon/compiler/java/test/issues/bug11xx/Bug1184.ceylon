class Bug1184() {
    shared String narySequenced(String s1, String s2, String s3, String s4, String* s) 
        => "narySequenced(``s1``, ``s2``, ``s3``, ``s4``; ``s``)";

    Bug1184 self() => this; 

    shared void bug(Bug1184 bug) {
        value r = narySequenced;
        value r2 = bug.narySequenced;
        value r3 = self().narySequenced;
    }
}