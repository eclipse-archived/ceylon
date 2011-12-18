doc "Determine if the arguments are identical. Equivalent to
     `x==y`."
see (identityHash)
shared Boolean identical(IdentifiableObject x, 
                         IdentifiableObject y) {
    return x==y;
}