import ceylon.language.meta.model { ClassModel }

"Returns the closed type and model of a given instance. Since only classes
 can be instantiated, this will always be a [[ClassModel]] model."
see(`function classDeclaration`)
shared native ClassModel<Type,Nothing> type<out Type>(Type instance)
    given Type satisfies Anything;
