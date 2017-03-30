import ceylon.language.meta.model {
    Attribute
}

shared interface Model<Parent,Result> given Result<T> {
    shared formal Result<Type> get<Type>(Attribute<Parent,Type> attribute);
}

shared interface Expression<T> satisfies Model<T,Expression> {
    shared actual Expression<Type> get<Type>(Attribute<T,Type> attribute)
            => object satisfies Expression<Type> {};
}
