import ceylon.language.meta.model { Type, IntersectionType }

native class AppliedIntersectionType<out Union=Anything>(shared Anything tipo, satisfiedTypes) satisfies IntersectionType<Union> {
  shared actual List<Type<Union>> satisfiedTypes;

  shared native actual Boolean typeOf(Anything instance);
  shared native actual Boolean supertypeOf(Type<Anything> type);
  shared native actual Boolean exactly(Type<Anything> type);

  shared native actual String string;
  shared native actual Integer hash;
  shared native actual Boolean equals(Object other);
}
