import ceylon.language.meta.model { Class, ClosedType = Type }

shared native Class<Type,Nothing> type<out Type>(Type instance)
    given Type satisfies Anything;

shared native ClosedType<Type> typeLiteral<out Type>()
    given Type satisfies Anything;
