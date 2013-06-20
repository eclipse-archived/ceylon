"Determine if the arguments are identical. Equivalent to
 `x===y`. Only instances of `Identifiable` have 
 well-defined identity."
see (`identityHash`)
shared Boolean identical(
        "An object with well-defined identity."
        Identifiable x, 
        "A second object with well-defined identity."
        Identifiable y) 
                => x===y;