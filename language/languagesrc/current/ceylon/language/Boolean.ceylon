shared object true extends Boolean("true") {}
shared object false extends Boolean("false") {}
shared abstract class Boolean(String name) 
        of true | false 
        extends Case(name) {}