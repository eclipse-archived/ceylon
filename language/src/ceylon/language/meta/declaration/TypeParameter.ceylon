"A type parameter declaration."
shared interface TypeParameter satisfies Declaration {
    
    // FIXME: make it NestableDeclaration&GenericDeclaration?
    "The declaration that declared this type parameter. This is either a [[ClassOrInterfaceDeclaration]] or a
     [[FunctionDeclaration]]."
    shared formal NestableDeclaration container;
    
    "True if this type parameter has a default type argument and can be omitted."
    shared formal Boolean defaulted;
    
    "This type parameter's default type argument, if it has one."
    shared formal OpenType? defaultTypeArgument;
    
    "This type parameter's variance, as defined by `in` or `out` keywords."
    shared formal Variance variance;
    
    "The `satisfies` upper bounds for this type parameter."
    shared formal OpenType[] satisfiedTypes;

    "The `of` enumerated bounds for this type parameter."
    shared formal OpenType[] caseTypes;
}