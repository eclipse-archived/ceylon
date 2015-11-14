"Variance information."
shared interface Variance of invariant | covariant | contravariant {}

"Invariant means that neither subtype nor supertype can be accepted, the
 type has to be exactly that which is declared."
shared object invariant satisfies Variance {
    string => "Invariant";
}

"Covariant means that subtypes of the given type may be returned."
shared object covariant satisfies Variance {
    string => "Covariant";
}

"Contravariant means that supertypes of the given type may be accepted."
shared object contravariant satisfies Variance {
    string => "Contravariant";
}
