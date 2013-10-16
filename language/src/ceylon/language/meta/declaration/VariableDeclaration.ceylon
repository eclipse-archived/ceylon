import ceylon.language.meta.model { Variable, VariableAttribute, AppliedType = Type, IncompatibleTypeException }

"Declaration of an attribute that is `variable` or has an `assign` block."
shared interface VariableDeclaration
        satisfies ValueDeclaration {
    
    "True."
    shared actual Boolean variable => true;

    see(`function ValueDeclaration.apply`)
    shared actual formal Variable<Type> apply<Type>();

    see(`function ValueDeclaration.memberApply`)
    shared actual formal VariableAttribute<Container, Type> memberApply<Container, Type>(AppliedType<Container> containerType);

    "Sets the current value of this toplevel value."
    shared default void set(Anything newValue)
        => apply<Anything>().unsafeSet(newValue);

    "Sets the current value of this attribute on the given container instance."
    throws(`class IncompatibleTypeException`, "If the specified container or new value type is not compatible with this attribute.")
    shared formal void memberSet(Object container, Anything newValue);
        //=> memberApply<Nothing, Anything>(`Nothing`).bind(container).unsafeSet(newValue);

    "Returns the setter declaration for this variable.
     
     For modelling purposes `variable` reference 
     values have a SetterDeclaration even though there is no 
     such setter explicit in the source code."
    shared formal SetterDeclaration setter;
}

