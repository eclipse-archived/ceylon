import ceylon.language.meta.model {
  ClosedType=Type,
  ClassOrInterface, Method, Function, TypeArgument
}
import ceylon.language.meta.declaration {
  FunctionDeclaration,
  TypeParameter
}

shared native class AppliedMethod<in Container, out Type=Anything, in Arguments=Nothing>()
    satisfies Method<Container,Type,Arguments>
    given Arguments satisfies Anything[] {

  shared actual native ClosedType<Anything>[] parameterTypes;
  shared actual native Function<Type, Arguments> bind(Object container);

  shared actual native FunctionDeclaration declaration;
  shared actual native ClosedType<Type> type;
  shared actual native ClassOrInterface<Anything> declaringType;

  shared actual native ClassOrInterface<Anything>? container;

  shared actual native Map<TypeParameter, ClosedType<Anything>> typeArguments;
  shared actual native ClosedType<Anything>[] typeArgumentList;
  shared actual native Map<TypeParameter, TypeArgument> typeArgumentWithVariances;
  shared actual native TypeArgument[] typeArgumentWithVarianceList;

  shared actual native String string;
  shared actual native Integer hash;
  shared actual native Boolean equals(Object other);
}
