@nomodel
deprecated("A deprecated class")
shared class Deprecated(String s="") {
}

@nomodel
shared class DeprecatedMembers {
    deprecated("default constructor")
    shared new (String s = "") {}
    
    deprecated("named constructor")
    shared new named(String s = "") {}
    
    deprecated("value constructor")
    shared new val {}
    
    deprecated{ 
        reason = "A deprecated method"; 
    }
    shared void m(String s="") {}
}

@nomodel
deprecated("A deprecated function")
shared void deprecatedFunction(String s="") {
    
}

@nomodel
deprecated("A deprecated value")
shared String deprecatedValue => "";

@nomodel
deprecated("A deprecated aliase")
alias DeprecatedAlias => String;