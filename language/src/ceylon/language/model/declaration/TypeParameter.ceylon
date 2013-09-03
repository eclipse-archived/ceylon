shared interface TypeParameter satisfies Declaration {
    shared formal Boolean defaulted;
    // FIXME: rename to 'default'?
    shared formal OpenType? defaultValue;
    // FIXME: replace this with an enumish?
    shared formal Boolean invariant;
    shared formal Boolean covariant;
    shared formal Boolean contravariant;
    
    // FIXME: the spec calls these both satisfiedTypes and upper bounds
    // FIXME: this could in fact be an IntersectionType, no?
    shared formal OpenType[] upperBounds;

    // FIXME: the spec calls these both caseTypes and enumerated bounds
    // FIXME: this could in fact be a UnionType, no?
    shared formal OpenType[] enumeratedBounds;
}