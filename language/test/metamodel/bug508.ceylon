import ceylon.language.meta.declaration { ClassDeclaration }
import ceylon.language.meta.model { Attribute, IncompatibleTypeException }

abstract class Bug508Object() {
    shared formal class FormalClass(){}
}
class Bug508Person() extends Bug508Object() {
    shared Integer personAttribute = 2;
    shared Integer personMethod() => 2;
    shared interface PersonInterface {}
    shared class PersonClass() {}
    shared actual class FormalClass() extends super.FormalClass() {}
}
class Bug508Gavin() extends Bug508Person() {
    shared Integer gavinAttribute = 3;
    shared Integer gavinMethod() => 3;
    shared interface GavinInterface {}
    shared class GavinClass() {}
}
class Bug508Gavin2() extends Bug508Person() {
    shared Integer gavin2Attribute = 3;
    shared Integer gavin2Method() => 3;
    shared interface Gavin2Interface {}
    shared class Gavin2Class() {}
}

@test
shared void bug508() {
    value gavin = Bug508Gavin();
    value gavinClass = `Bug508Gavin`;
    
    //
    // Get a single one
    
    assert(eq(gavinClass.getAttribute<Anything>("hash"), null));
    assert(eq(gavinClass.getAttribute<Identifiable>("hash"), `Identifiable.hash`));
    assert(eq(gavinClass.getAttribute<Object>("hash"), `Object.hash`));
    assert(eq(gavinClass.getAttribute<Nothing>("hash"), `Identifiable.hash`));
    assert(eq(gavinClass.getAttribute<Bug508Gavin>("hash"), `Identifiable.hash`));
    assert(eq(gavinClass.getAttribute<Bug508Gavin>("gavinAttribute"), `Bug508Gavin.gavinAttribute`));

    assert(eq(gavinClass.getDeclaredAttribute<Bug508Gavin>("hash"), null));
    try{
        gavinClass.getDeclaredAttribute<Object>("gavinAttribute");
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(eq(gavinClass.getDeclaredAttribute<Bug508Gavin>("gavinAttribute"), `Bug508Gavin.gavinAttribute`));
    assert(eq(gavinClass.getDeclaredAttribute<Nothing>("gavinAttribute"), `Bug508Gavin.gavinAttribute`));

    assert(eq(gavinClass.getMethod<Anything>("equals"), null));
    assert(eq(gavinClass.getMethod<Identifiable>("equals"), `Identifiable.equals`));
    assert(eq(gavinClass.getMethod<Object>("equals"), `Object.equals`));
    assert(eq(gavinClass.getMethod<Nothing>("equals"), `Identifiable.equals`));
    assert(eq(gavinClass.getMethod<Bug508Gavin>("equals"), `Identifiable.equals`));

    assert(eq(gavinClass.getDeclaredMethod<Bug508Gavin>("equals"), null));
    try{
        gavinClass.getDeclaredMethod<Object>("gavinMethod");
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(eq(gavinClass.getDeclaredMethod<Bug508Gavin>("gavinMethod"), `Bug508Gavin.gavinMethod`));
    assert(eq(gavinClass.getDeclaredMethod<Nothing>("gavinMethod"), `Bug508Gavin.gavinMethod`));

    assert(eq(gavinClass.getInterface<Object>("PersonInterface"), null));
    assert(eq(gavinClass.getInterface<Nothing>("PersonInterface"), `Bug508Person.PersonInterface`));
    assert(eq(gavinClass.getInterface<Bug508Gavin>("PersonInterface"), `Bug508Person.PersonInterface`));

    assert(eq(gavinClass.getDeclaredInterface<Bug508Gavin>("PersonInterface"), null));
    try{
        gavinClass.getDeclaredInterface<Object>("GavinInterface");
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(eq(gavinClass.getDeclaredInterface<Bug508Gavin>("GavinInterface"), `Bug508Gavin.GavinInterface`));
    assert(eq(gavinClass.getDeclaredInterface<Nothing>("GavinInterface"), `Bug508Gavin.GavinInterface`));

    assert(eq(gavinClass.getClass<Anything>("FormalClass"), null));
    assert(eq(gavinClass.getClass<Bug508Person>("FormalClass"), `Bug508Person.FormalClass`));
    assert(eq(gavinClass.getClass<Bug508Object>("FormalClass"), `Bug508Object.FormalClass`));
    assert(eq(gavinClass.getClass<Nothing>("FormalClass"), `Bug508Person.FormalClass`));
    assert(eq(gavinClass.getClass<Bug508Gavin>("FormalClass"), `Bug508Person.FormalClass`));

    assert(eq(gavinClass.getDeclaredClass<Bug508Gavin>("PersonClass"), null));
    try{
        gavinClass.getDeclaredClass<Object>("GavinClass");
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(eq(gavinClass.getDeclaredClass<Bug508Gavin>("GavinClass"), `Bug508Gavin.GavinClass`));
    assert(eq(gavinClass.getDeclaredClass<Nothing>("GavinClass"), `Bug508Gavin.GavinClass`));

    assert(eq(gavinClass.getClassOrInterface<Anything>("FormalClass"), null));
    assert(eq(gavinClass.getClassOrInterface<Bug508Person>("FormalClass"), `Bug508Person.FormalClass`));
    assert(eq(gavinClass.getClassOrInterface<Bug508Object>("FormalClass"), `Bug508Object.FormalClass`));
    assert(eq(gavinClass.getClassOrInterface<Nothing>("FormalClass"), `Bug508Person.FormalClass`));
    assert(eq(gavinClass.getClassOrInterface<Bug508Gavin>("FormalClass"), `Bug508Person.FormalClass`));

    assert(eq(gavinClass.getClassOrInterface<Anything>("PersonInterface"), null));
    assert(eq(gavinClass.getClassOrInterface<Bug508Person>("PersonInterface"), `Bug508Person.PersonInterface`));
    assert(eq(gavinClass.getClassOrInterface<Bug508Gavin>("PersonInterface"), `Bug508Person.PersonInterface`));
    assert(eq(gavinClass.getClassOrInterface<Nothing>("PersonInterface"), `Bug508Person.PersonInterface`));
    
    assert(eq(gavinClass.getDeclaredClassOrInterface<Bug508Gavin>("PersonClass"), null));
    try{
        gavinClass.getDeclaredClassOrInterface<Object>("GavinClass");
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(eq(gavinClass.getDeclaredClassOrInterface<Bug508Gavin>("GavinClass"), `Bug508Gavin.GavinClass`));
    assert(eq(gavinClass.getDeclaredClassOrInterface<Nothing>("GavinClass"), `Bug508Gavin.GavinClass`));

    assert(eq(gavinClass.getDeclaredClassOrInterface<Bug508Gavin>("PersonInterface"), null));
    try{
        gavinClass.getDeclaredClassOrInterface<Object>("GavinInterface");
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(eq(gavinClass.getDeclaredClassOrInterface<Bug508Gavin>("GavinInterface"), `Bug508Gavin.GavinInterface`));
    assert(eq(gavinClass.getDeclaredClassOrInterface<Nothing>("GavinInterface"), `Bug508Gavin.GavinInterface`));

    //
    // Get them all

    // on a supertype
    
    value objectAttributes = gavinClass.getAttributes<Object>();
    assert(objectAttributes.size == 2);
    assert(objectAttributes.contains(`Object.hash`));
    assert(objectAttributes.contains(`Object.string`));

    value objectMethods = gavinClass.getMethods<Object>();
    assert(objectMethods.size == 1);
    assert(objectMethods.contains(`Object.equals`));

    value objectInterfaces = gavinClass.getInterfaces<Bug508Person>();
    assert(objectInterfaces.size == 1);
    assert(objectInterfaces.contains(`Bug508Person.PersonInterface`));

    value objectClasses = gavinClass.getClasses<Bug508Person>();
    assert(objectClasses.size == 2);
    assert(objectClasses.contains(`Bug508Person.PersonClass`));
    assert(objectClasses.contains(`Bug508Person.FormalClass`));

    // on a supertype with no member

    value objectAttributes1 = gavinClass.getAttributes<Anything>();
    assert(objectAttributes1.size == 0);

    value objectMethods1 = gavinClass.getMethods<Anything>();
    assert(objectMethods1.size == 0);

    value objectInterfaces1 = gavinClass.getInterfaces<Anything>();
    assert(objectInterfaces1.size == 0);

    value objectClasses1 = gavinClass.getClasses<Anything>();
    assert(objectClasses1.size == 0);

    // on the current type

    value objectAttributes2 = gavinClass.getAttributes<Bug508Gavin>();
    assert(objectAttributes2.size == 4);
    assert(objectAttributes2.contains(`Identifiable.hash`));
    assert(objectAttributes2.contains(`Object.string`));
    assert(objectAttributes2.contains(`Bug508Gavin.gavinAttribute`));
    assert(objectAttributes2.contains(`Bug508Person.personAttribute`));

    value objectMethods2 = gavinClass.getMethods<Bug508Gavin>();
    assert(objectMethods2.size == 3);
    assert(objectMethods2.contains(`Identifiable.equals`));
    assert(objectMethods2.contains(`Bug508Gavin.gavinMethod`));
    assert(objectMethods2.contains(`Bug508Person.personMethod`));

    value objectInterfaces2 = gavinClass.getInterfaces<Bug508Gavin>();
    assert(objectInterfaces2.size == 2);
    assert(objectInterfaces2.contains(`Bug508Gavin.GavinInterface`));
    assert(objectInterfaces2.contains(`Bug508Person.PersonInterface`));

    value objectClasses2 = gavinClass.getClasses<Bug508Gavin>();
    assert(objectClasses2.size == 3);
    assert(objectClasses2.contains(`Bug508Gavin.GavinClass`));
    assert(objectClasses2.contains(`Bug508Person.PersonClass`));
    assert(objectClasses2.contains(`Bug508Person.FormalClass`));

    // on a different type with a common Person ancestor

    value objectAttributes3 = gavinClass.getAttributes<Bug508Gavin2>();
    assert(objectAttributes3.size == 3);
    assert(objectAttributes3.contains(`Identifiable.hash`));
    assert(objectAttributes3.contains(`Object.string`));
    assert(objectAttributes3.contains(`Bug508Person.personAttribute`));

    value objectMethods3 = gavinClass.getMethods<Bug508Gavin2>();
    assert(objectMethods3.size == 2);
    assert(objectMethods3.contains(`Identifiable.equals`));
    assert(objectMethods3.contains(`Bug508Person.personMethod`));

    value objectInterfaces3 = gavinClass.getInterfaces<Bug508Gavin2>();
    assert(objectInterfaces3.size == 1);
    assert(objectInterfaces3.contains(`Bug508Person.PersonInterface`));

    value objectClasses3 = gavinClass.getClasses<Bug508Gavin2>();
    assert(objectClasses3.size == 2);
    assert(objectClasses3.contains(`Bug508Person.FormalClass`));
    assert(objectClasses3.contains(`Bug508Person.PersonClass`));
    
    //
    // Get the declared list

    // on a supertype
        
    value objectDeclaredAttributes = gavinClass.getDeclaredAttributes<Object>();
    assert(objectDeclaredAttributes.size == 0);
    
    value objectDeclaredMethods = gavinClass.getDeclaredMethods<Object>();
    assert(objectDeclaredMethods.size == 0);

    value objectDeclaredClasses = gavinClass.getDeclaredClasses<Object>();
    assert(objectDeclaredClasses.size == 0);

    value objectDeclaredInterfaces = gavinClass.getDeclaredInterfaces<Object>();
    assert(objectDeclaredInterfaces.size == 0);
    
    // on this type
    
    value objectDeclaredAttributes1 = gavinClass.getDeclaredAttributes<Bug508Gavin>();
    assert(objectDeclaredAttributes1.size == 1);
    assert(objectDeclaredAttributes1.contains(`Bug508Gavin.gavinAttribute`));
    
    value objectDeclaredMethods1 = gavinClass.getDeclaredMethods<Bug508Gavin>();
    assert(objectDeclaredMethods1.size == 1);
    assert(objectDeclaredMethods1.contains(`Bug508Gavin.gavinMethod`));
    
    value objectDeclaredClasses1 = gavinClass.getDeclaredClasses<Bug508Gavin>();
    assert(objectDeclaredClasses1.size == 1);
    assert(objectDeclaredClasses1.contains(`Bug508Gavin.GavinClass`));
    
    value objectDeclaredInterfaces1 = gavinClass.getDeclaredInterfaces<Bug508Gavin>();
    assert(objectDeclaredInterfaces1.size == 1);
    assert(objectDeclaredInterfaces1.contains(`Bug508Gavin.GavinInterface`));

    // on a sub-type
    
    value objectDeclaredAttributes2 = gavinClass.getDeclaredAttributes<Nothing>();
    assert(objectDeclaredAttributes2.size == 1);
    assert(objectDeclaredAttributes2.contains(`Bug508Gavin.gavinAttribute`));
    
    value objectDeclaredMethods2 = gavinClass.getDeclaredMethods<Nothing>();
    assert(objectDeclaredMethods2.size == 1);
    assert(objectDeclaredMethods2.contains(`Bug508Gavin.gavinMethod`));
    
    value objectDeclaredClasses2 = gavinClass.getDeclaredClasses<Nothing>();
    assert(objectDeclaredClasses2.size == 1);
    assert(objectDeclaredClasses2.contains(`Bug508Gavin.GavinClass`));
    
    value objectDeclaredInterfaces2 = gavinClass.getDeclaredInterfaces<Nothing>();
    assert(objectDeclaredInterfaces2.size == 1);
    assert(objectDeclaredInterfaces2.contains(`Bug508Gavin.GavinInterface`));
    
}
