shared interface Value<out Type>
        satisfies Declaration {
    
    shared formal Type get();
}