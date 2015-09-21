import ceylon.language.meta.declaration{
    Declaration,Package
}
"A model element that has a declaration."
shared sealed interface Declared {
    
    "The declaration model of this model."
    shared formal Declaration declaration;
    
    "The container type of this model, or `null` if this is a toplevel model."
    shared formal Type<>|Package? container;
}