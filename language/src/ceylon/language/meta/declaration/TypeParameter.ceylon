shared interface TypeParameter satisfies Declaration {
    
    shared formal NestableDeclaration container;
    
    shared formal Boolean defaulted;
    
    shared formal OpenType? defaultTypeArgument;
    
    shared formal Variance variance;
    
    shared formal OpenType[] satisfiedTypes;

    shared formal OpenType[] caseTypes;
}