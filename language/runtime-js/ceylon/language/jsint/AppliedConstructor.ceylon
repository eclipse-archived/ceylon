import ceylon.language.meta.declaration {
  Module, ConstructorDeclaration
}
import ceylon.language.meta.model {
  Constructor, Class,
  ClosedType=Type
}

shared native class AppliedConstructor<out Type=Anything, in Arguments=Nothing>
    (Anything tipo)
    satisfies Constructor<Type,Arguments>
    given Arguments satisfies Anything[] {
  shared native actual ConstructorDeclaration declaration;
  shared native actual Class<Type> container;
  //Functional
  shared native actual ClosedType<Anything>[] parameterTypes;
  //Applicable
  shared actual native Type apply(Anything* arguments);
  shared actual native Type namedApply({<String->Anything>*} arguments);
  shared actual native String string;
}
