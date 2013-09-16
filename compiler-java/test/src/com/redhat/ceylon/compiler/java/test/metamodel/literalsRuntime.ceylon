import ceylon.language.meta.model { Class }

void literalsRuntime(){
    // make sure that literals work
    assert(`class LitClass`.name == "LitClass");
    assert(`LitClass`.declaration.name == "LitClass");

    // FIXME: test the Callable side of things
    // FIXME: ask Tom how to implement this:
    //`LitClass`(1);

    assert(`class LitParameterisedClass`.name == "LitParameterisedClass");
    assert(`LitParameterisedClass<Integer>`.declaration.name == "LitParameterisedClass");
    
    assert(`class LitClass.Member`.name == "Member");
    assert(`LitClass.Member`.declaration.name == "Member");

    assert(`class LitParameterisedClass.Member`.name == "Member");
    assert(`LitParameterisedClass<Integer>.Member<String>`.declaration.name == "Member");
    
    assert(`interface LitInterface`.name == "LitInterface");
    assert(`LitInterface`.declaration.name == "LitInterface");

    assert(`interface LitInterface.Member`.name == "Member");
    assert(`LitInterface.Member`.declaration.name == "Member");
    
    assert(`value litValue`.name == "litValue");
    assert(`litValue`.declaration.name == "litValue");

    assert(`value LitClass.attribute`.name == "attribute");
    assert(`LitClass.attribute`.declaration.name == "attribute");

    assert(`value LitParameterisedClass.attribute`.name == "attribute");
    assert(`LitParameterisedClass<Integer>.attribute`.declaration.name == "attribute");
    
    assert(`value litVariable`.name == "litVariable");
    assert(`litVariable`.declaration.name == "litVariable");

    assert(`value LitClass.variableAttribute`.name == "variableAttribute");
    assert(`LitClass.variableAttribute`.declaration.name == "variableAttribute");

    assert(`value LitParameterisedClass.variableAttribute`.name == "variableAttribute");
    assert(`LitParameterisedClass<Integer>.variableAttribute`.declaration.name == "variableAttribute");

    assert(`function litFunction`.name == "litFunction");
    assert(`litFunction`.declaration.name == "litFunction");

    assert(`function LitClass.method`.name == "method");
    assert(`LitClass.method`.declaration.name == "method");

    assert(`function LitParameterisedClass.method`.name == "method");
    assert(`LitParameterisedClass<Integer>.method`.declaration.name == "method");
    
    assert(`function LitClass.parameterisedMethod`.name == "parameterisedMethod");
    assert(`LitClass.parameterisedMethod<Integer>`.declaration.name == "parameterisedMethod");

    assert(`function LitParameterisedClass.parameterisedMethod`.name == "parameterisedMethod");
    assert(`LitParameterisedClass<Integer>.parameterisedMethod<Integer>`.declaration.name == "parameterisedMethod");
    
    assert(`LitClassWithParameters.parameterAndSharedAttribute`.declaration.name == "parameterAndSharedAttribute"); // on attribute
    assert(`value LitClassWithParameters.parameterAndSharedAttribute`.defaulted == false); // on param decl

    // not supported yet
    //assert(`LitClassWithParameters.attributeAndParameter`.declaration.name == "attributeAndParameter"); // on function
    //assert(`LitClassWithParameters.attributeAndParameter`.defaulted == false); // on param decl
    //assert(`LitClassWithParameters.attributeAndParameter`.parameterDeclarations.size == 0); // on function decl

    assert(`alias Alias`.name == "Alias");
    assert(is Class<LitClass,Nothing> aliasModel = `Alias`, aliasModel.declaration.name == "LitClass");
}