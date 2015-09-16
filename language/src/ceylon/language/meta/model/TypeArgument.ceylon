import ceylon.language.meta.declaration {
    Variance
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"A tuple representing a type argument and its use-site variance."
shared alias TypeArgument => [ClosedType<>,Variance];
