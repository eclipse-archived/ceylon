import ceylon.language.meta.declaration {
  CallableConstructorDeclaration, TypeParameter
}
import ceylon.language.meta.model {
  CallableConstructor, ClassModel,
  ClosedType=Type, TypeArgument//, Function
}

shared native class AppliedCallableConstructor<out Type=Anything, in Arguments=Nothing>(Anything _tipo)
    satisfies CallableConstructor<Type,Arguments>// & Function<Type,Arguments>
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
  shared native actual Map<TypeParameter, ClosedType> typeArguments;
  shared native actual ClosedType<Anything>[] typeArgumentList;
  shared actual native Map<TypeParameter, TypeArgument> typeArgumentWithVariances;
  shared actual native TypeArgument[] typeArgumentWithVarianceList;
  shared native actual Boolean equals(Object other);
}
