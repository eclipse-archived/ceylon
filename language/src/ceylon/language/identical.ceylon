doc "Determine if the arguments are identical. Equivalent to
     `x===y`. Only instances of `Identifiable` have 
     well-defined identity."
see (identityHash)
shared Boolean identical(
        doc "An object with well-defined identity."
        Identifiable x, 
        doc "A second object with well-defined identity."
        Identifiable y) 
                => x===y;