shared interface Variance of invariant | covariant | contravariant {}

shared object invariant satisfies Variance {
    string => "Invariant";
}
shared object covariant satisfies Variance {
    string => "Covariant";
}
shared object contravariant satisfies Variance {
    string => "contravariant";
}
