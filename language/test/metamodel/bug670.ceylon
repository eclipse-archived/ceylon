import ceylon.language.meta.declaration { ValueDeclaration }

class Bug670() {
    shared actual variable String string = "x";
}

@test
shared void bug670() {
    value fooString = Bug670();
    fooString.string = "y"; // ok, of course
    
    `Bug670`.getAttribute<Bug670,String,String>("string")
            ?.bind(fooString)
            ?.set("updated1"); //error
    //ceylon run: Incompatible type: actual type of applied declaration is
    //Attribute<FooString,String,Nothing> is not compatible with expected type:
    //Attribute<FooString,String,String>. Try passing the type argument explicitly
    //with: memberApply<FooString,String,Nothing>()
    
    `Bug670`.getAttribute<Bug670>("string")
            ?.bind(fooString)
            ?.setIfAssignable("updated2"); // error    
    //ceylon run: Value is not mutable
    //ceylon.language.meta.model.MutationException "Value is not mutable"
    
    assert(`value Bug670.string`.variable); // ** false **
}
