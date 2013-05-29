import ceylon.language.metamodel {
    ClosedType = Type
}

shared native Class<Type,Nothing> type<out Type>(Type instance)
    given Type satisfies Anything;

shared native ClosedType typeLiteral<out Type>()
    given Type satisfies Anything;
