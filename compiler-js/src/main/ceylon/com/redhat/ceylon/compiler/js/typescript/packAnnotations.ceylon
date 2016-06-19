"Pack up common annotations in a single integer (bitfield).
 See `MetamodelGenerator.annotationBits`."
Integer packAnnotations(
    Boolean shared = false,
    Boolean actual = false,
    Boolean formal = false,
    Boolean default = false,
    Boolean sealed = false,
    Boolean final = false,
    Boolean native = false,
    Boolean late = false,
    Boolean abstract = false,
    Boolean annotation = false,
    Boolean variable = false,
    Boolean serializable = false)
        => 0.set(0, shared)
            .set(1, actual)
            .set(2, formal)
            .set(3, default)
            .set(4, sealed)
            .set(5, final)
            .set(6, native)
            .set(7, late)
            .set(8, abstract)
            .set(9, annotation)
            .set(10, variable)
            .set(11, serializable);
