import ceylon.language.meta.model { Type, UnionType }

native class AppliedUnionType<out Union=Anything>(shared Anything tipo, caseTypes) satisfies UnionType<Union> {
  shared actual List<Type<Union>> caseTypes;

  shared native actual Boolean typeOf(Anything instance);
  shared native actual Boolean supertypeOf(Type<Anything> type);
  shared native actual Boolean exactly(Type<Anything> type);
  shared native actual String string;
  shared native actual Integer hash;
  shared native actual Boolean equals(Object other);
}
