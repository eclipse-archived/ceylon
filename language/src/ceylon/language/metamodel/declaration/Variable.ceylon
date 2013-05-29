"Model of a value that is `variable` or has an `assign` block."
shared interface Variable
        satisfies Value {
    
    "Returns a model of the setter of this variable.
     
     For modelling purposes `variable` reference 
     values have a Setter even though there is no 
     such setter explicit in the source code."
    shared formal Setter setter;
}

