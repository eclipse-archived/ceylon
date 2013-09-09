import ceylon.language.model.declaration {
  FunctionDeclaration, ValueDeclaration, ClassDeclaration, InterfaceDeclaration,
  Package, FunctionOrValueDeclaration,
  TopLevelOrMemberDeclaration, OpenType, TypeParameter,
  OpenParameterisedType, AnnotatedDeclaration
}
import ceylon.language.model {
    AppliedInterface = Interface,
    AppliedClass = Class,
    AppliedFunction = Function,
    Method, Value,
    AppliedMember = Member,
    AppliedClassOrInterface = ClassOrInterface,
    MetamodelAnnotation=Annotation,
    Type
}

class OpenFunction(shared actual String name, shared actual Package packageContainer, shared actual Boolean toplevel) satisfies FunctionDeclaration {

    shared actual AppliedFunction<Anything, Nothing> apply(Type<Anything>* types) { throw; }
    shared actual AppliedFunction<Anything, Nothing> bindAndApply(Object instance, Type<Anything>* types) { throw; }
    shared actual Method<Container, MethodType, Arguments> memberApply<Container, MethodType, Arguments>(Type<Anything>* types)
        given Arguments satisfies Anything[] { throw; }

    shared actual Boolean defaulted => false;
    shared actual Boolean variadic => false;

    shared actual FunctionOrValueDeclaration[] parameterDeclarations => [];
    shared actual FunctionOrValueDeclaration? getParameterDeclaration(String name) => null;
  shared actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies MetamodelAnnotation<Annotation> => [];

  shared actual OpenType openType { throw; }
  shared actual TypeParameter[] typeParameterDeclarations => [];
  shared actual TypeParameter? getTypeParameterDeclaration(String name) => null;

  shared actual Boolean actual { return false; }
  shared actual Boolean formal { return false; }
  shared actual Boolean default { return false; }
  shared actual Boolean shared { return false; }
  shared actual String qualifiedName { return name; }
  shared actual AnnotatedDeclaration container { return packageContainer; }
}

class OpenValue(shared actual String name, shared actual Package packageContainer, shared actual Boolean toplevel) satisfies ValueDeclaration {
    shared actual Value<Anything> apply(Anything instance) { throw; }
    shared actual Boolean defaulted => false;
    shared actual Boolean variadic => false;
  shared actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies MetamodelAnnotation<Annotation> => [];
  shared actual OpenType openType { throw; }

  shared actual Boolean actual { return false; }
  shared actual Boolean formal { return false; }
  shared actual Boolean default { return false; }
  shared actual Boolean shared { return false; }
  shared actual String qualifiedName { return name; }
  shared actual AnnotatedDeclaration container { return packageContainer; }
}

class OpenClass(shared actual String name, shared actual Package packageContainer, shared actual Boolean toplevel) satisfies ClassDeclaration {
  shared actual Boolean anonymous => false;
  shared actual AppliedClass<Anything, Nothing> apply(Type<Anything>* types) { throw; }
  shared actual AppliedClass<Anything, Nothing> bindAndApply(Object instance, Type<Anything>* types) { throw; }
  shared actual AppliedMember<Container, Kind> memberApply<Container, Kind>(Type<Anything>* types)
        given Kind satisfies AppliedClassOrInterface<Anything> { throw; }
  shared actual Kind[] memberDeclarations<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration => [];
  shared actual Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration => [];
  shared actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies MetamodelAnnotation<Annotation> => [];
  shared actual Kind? getMemberDeclaration<Kind>(String name) 
            given Kind satisfies TopLevelOrMemberDeclaration => null;
  shared actual OpenType openType { throw; }
  shared actual TypeParameter[] typeParameterDeclarations => [];
  shared actual TypeParameter? getTypeParameterDeclaration(String name) => null;

  shared actual FunctionOrValueDeclaration[] parameterDeclarations => [];
  shared actual FunctionOrValueDeclaration? getParameterDeclaration(String name) => null;

  shared actual OpenParameterisedType<ClassDeclaration>? superclassDeclaration => null;
  shared actual OpenParameterisedType<InterfaceDeclaration>[] interfaceDeclarations => [];

  shared actual Boolean actual { return false; }
  shared actual Boolean formal { return false; }
  shared actual Boolean default { return false; }
  shared actual Boolean shared { return false; }
  shared actual String qualifiedName { return name; }
  shared actual AnnotatedDeclaration container { return packageContainer; }

  shared actual Boolean abstract { return false; }
  shared actual OpenType[] caseTypes { return []; }

  shared actual Boolean isAlias => false;
}

class OpenInterface(shared actual String name, shared actual Package packageContainer, shared actual Boolean toplevel) satisfies InterfaceDeclaration {
  shared actual AppliedInterface<Anything> apply(Type<Anything>* types) { throw; }
  shared actual AppliedInterface<Anything> bindAndApply(Object instance, Type<Anything>* types) { throw; }
  shared actual AppliedMember<Container, Kind> memberApply<Container, Kind>(Type<Anything>* types)
        given Kind satisfies AppliedClassOrInterface<Anything> { throw; }

  shared actual Kind[] memberDeclarations<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration => [];
  shared actual Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration => [];
  shared actual Annotation[] annotations<out Annotation>()
        given Annotation satisfies MetamodelAnnotation<Annotation> => [];
  shared actual Kind? getMemberDeclaration<Kind>(String name) 
            given Kind satisfies TopLevelOrMemberDeclaration => null;
  shared actual OpenType openType { throw; }
  shared actual TypeParameter[] typeParameterDeclarations => [];
  shared actual TypeParameter? getTypeParameterDeclaration(String name) => null;

  shared actual OpenParameterisedType<ClassDeclaration>? superclassDeclaration => null;
  shared actual OpenParameterisedType<InterfaceDeclaration>[] interfaceDeclarations => [];

  shared actual Boolean actual { return false; }
  shared actual Boolean formal { return false; }
  shared actual Boolean default { return false; }
  shared actual Boolean shared { return false; }
  shared actual String qualifiedName { return name; }
  shared actual AnnotatedDeclaration container { return packageContainer; }

  shared actual OpenType[] caseTypes { return []; }

  shared actual Boolean isAlias => false;
}
