
shared interface Value<out Type>
        satisfies AttributeModel<Type> {

    shared formal Type get();
}
