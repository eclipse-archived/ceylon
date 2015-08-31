import ceylon.language.meta.declaration {
  CallableConstructorDeclaration, TypeParameter
}
import ceylon.language.meta.model {
  CallableConstructor, ClassModel,
  ClosedType=Type
}

shared native class AppliedCallableConstructor<out Type=Anything, in Arguments=Nothing>(Anything _tipo)
    satisfies CallableConstructor<Type,Arguments>
    given Arguments satisfies Anything[] {
  shared native actual CallableConstructorDeclaration declaration;
  shared native actual ClassModel<Type> container;
  shared native actual ClassModel<Type> type;
  //Functional
  shared native actual ClosedType<Anything>[] parameterTypes;
  //Applicable
  shared native actual Type apply(Anything* arguments);
  shared native actual Type namedApply({<String->Anything>*} arguments);
  shared native actual String string;
  //Generic
  shared actual native Map<TypeParameter, ClosedType> typeArguments;
  shared actual native ClosedType<Anything>[] typeArgumentList;
}
