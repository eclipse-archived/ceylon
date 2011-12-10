doc "Abstract supertype for types which can be automatically
     widened to a different type in numeric operator 
     expressions. The type argument is a union of wider 
     types to which the subtype can be cast. 
     
     For example, `Integer` satisfies 
     `Castable<Integer|Integer|Float>` and `Integer` 
     satisfies Castable<Integer|Float>, so `Integer` can be 
     promoted to `Integer` in an expression like `1*-1`."
see (Integer, Integer)
by "Gavin"
shared interface Castable<in Types> {
    doc "Cast this object to the given type."
    shared formal CastValue castTo<CastValue>()
            given CastValue satisfies Types;
}
