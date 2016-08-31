import ceylon.language.meta.declaration {
    Variance
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"A tuple representing a type argument and its use-site variance."
since("1.2.0")
shared alias TypeArgument => [ClosedType<>,Variance];
