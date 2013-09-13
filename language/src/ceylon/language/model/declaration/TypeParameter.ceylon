shared interface TypeParameter satisfies Declaration {
    
    shared formal TopLevelOrMemberDeclaration container;
    
    shared formal Boolean defaulted;
    
    shared formal OpenType? defaultTypeArgument;
    
    shared formal Variance variance;
    
    // FIXME: the spec calls these both satisfiedTypes and upper bounds
    // FIXME: this could in fact be an IntersectionType, no?
    shared formal OpenType[] upperBounds;

    // FIXME: the spec calls these both caseTypes and enumerated bounds
    // FIXME: this could in fact be a UnionType, no?
    shared formal OpenType[] enumeratedBounds;
}