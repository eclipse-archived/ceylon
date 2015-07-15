import ceylon.language.meta.model {
  ClosedType=Type, Function, TypeArgument
}
import ceylon.language.meta.declaration {
  FunctionDeclaration, TypeParameter
}

shared native class AppliedFunction<out Type, in Arguments>()
    satisfies Function<Type,Arguments>
    given Arguments satisfies Anything[] {
  shared actual native ClosedType<Anything>[] parameterTypes;
  shared actual native Type apply(Anything* arguments);
  shared actual native Type namedApply(Iterable<String->Anything> arguments);

  shared actual native FunctionDeclaration declaration;
  shared actual native ClosedType<Type> type;
  shared actual native ClosedType<Anything>? container;
  shared actual native Map<TypeParameter, ClosedType> typeArguments;
  shared actual native ClosedType<Anything>[] typeArgumentList;
  shared actual native Map<TypeParameter, TypeArgument> typeArgumentWithVariances;
  shared actual native TypeArgument[] typeArgumentWithVarianceList;

  shared actual native String string;
  shared actual native Integer hash;
  shared actual native Boolean equals(Object other);
}
