import tsc {
    ...
}
import ceylon.json {
    JsonObject
}

JsonObject convertTypeForModel(type, parameterType = false) {
    "The type."
    Type type;
    "Whether this type is the type of a parameter.
     `number` is mapped to the more permissive `Float|Integer` in that case."
    Boolean parameterType;
    switch (type.flags)
    case (TypeFlags.String) {
        return stringTypeForModel;
    }
    case (TypeFlags.Number) {
        if (parameterType) {
            return floatOrIntegerTypeForModel;
        } else {
            return floatTypeForModel;
        }
    }
    else {
        throw AssertionError("Type of flags ``type.flags`` not implemented");
    }
}
