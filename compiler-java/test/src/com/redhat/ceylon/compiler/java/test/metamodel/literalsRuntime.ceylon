void literalsRuntime(){
    // make sure that literals work and are both Declaration and Model
    assert(`LitClass`.name == "LitClass");
    assert(`LitClass`.declaration.name == "LitClass");

    // FIXME: test the Callable side of things
    // FIXME: ask Tom how to implement this:
    //`LitClass`(1);

    assert(`LitParameterisedClass`.name == "LitParameterisedClass");
    assert(`LitParameterisedClass<Integer>`.declaration.name == "LitParameterisedClass");
    
    assert(`LitClass.Member`.name == "Member");
    assert(`LitClass.Member`.declaration.name == "Member");

    assert(`LitParameterisedClass.Member`.name == "Member");
    assert(`LitParameterisedClass<Integer>.Member<String>`.declaration.name == "Member");
    
    assert(`LitInterface`.name == "LitInterface");
    assert(`LitInterface`.declaration.name == "LitInterface");

    assert(`LitInterface.Member`.name == "Member");
    assert(`LitInterface.Member`.declaration.name == "Member");
    
    assert(`litValue`.name == "litValue");
    assert(`litValue`.declaration.name == "litValue");

    assert(`LitClass.attribute`.name == "attribute");
    assert(`LitClass.attribute`.declaration.name == "attribute");

    assert(`LitParameterisedClass.attribute`.name == "attribute");
    assert(`LitParameterisedClass<Integer>.attribute`.declaration.name == "attribute");
    
    assert(`litVariable`.name == "litVariable");
    assert(`litVariable`.declaration.name == "litVariable");

    assert(`LitClass.variableAttribute`.name == "variableAttribute");
    assert(`LitClass.variableAttribute`.declaration.name == "variableAttribute");

    assert(`LitParameterisedClass.variableAttribute`.name == "variableAttribute");
    assert(`LitParameterisedClass<Integer>.variableAttribute`.declaration.name == "variableAttribute");

    assert(`litFunction`.name == "litFunction");
    assert(`litFunction`.declaration.name == "litFunction");

    assert(`LitClass.method`.name == "method");
    assert(`LitClass.method`.declaration.name == "method");

    assert(`LitParameterisedClass.method`.name == "method");
    assert(`LitParameterisedClass<Integer>.method`.declaration.name == "method");
    
    assert(`LitClass.parameterisedMethod`.name == "parameterisedMethod");
    assert(`LitClass.parameterisedMethod<Integer>`.declaration.name == "parameterisedMethod");

    assert(`LitParameterisedClass.parameterisedMethod`.name == "parameterisedMethod");
    assert(`LitParameterisedClass<Integer>.parameterisedMethod<Integer>`.declaration.name == "parameterisedMethod");
    
    assert(`LitClassWithParameters.parameterAndSharedAttribute`.declaration.name == "parameterAndSharedAttribute"); // on attribute
    assert(`LitClassWithParameters.parameterAndSharedAttribute`.defaulted == false); // on param decl

    assert(`LitClassWithParameters.parameterAttribute`.declaration.name == "parameterAttribute"); // on attribute
    assert(`LitClassWithParameters.parameterAttribute`.defaulted == false); // on param decl

    // not supported yet
    //assert(`LitClassWithParameters.attributeAndParameter`.declaration.name == "attributeAndParameter"); // on function
    //assert(`LitClassWithParameters.attributeAndParameter`.defaulted == false); // on param decl
    //assert(`LitClassWithParameters.attributeAndParameter`.parameterDeclarations.size == 0); // on function decl
}