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
    if (hasTypeFlag(type.flags, TypeFlags.\iString)) {
        return stringTypeForModel;
    } else if (hasTypeFlag(type.flags, TypeFlags.\iNumber)) {
        if (parameterType) {
            return floatOrIntegerTypeForModel;
        } else {
            return floatTypeForModel;
        }
    } else if (hasTypeFlag(type.flags, TypeFlags.\iBoolean)) {
        return booleanTypeForModel;
    } else if (hasTypeFlag(type.flags, TypeFlags.\iVoid) || hasTypeFlag(type.flags, TypeFlags.\iAny)) {
        return anythingTypeForModel;
    } else if (hasTypeFlag(type.flags, TypeFlags.\iReference) || hasTypeFlag(type.flags, TypeFlags.\iInterface)) {
        // flags also contain e. g. TypeFlags.\iClass – I think we can ignore that
        dynamic {
            dynamic symbol = eval("(function(x){return x.symbol})")(type); // TODO use assert
            String name = symbol.name;
            // assume it’s from the current module; TODO find out how to discover the defining module of type symbol
            return JsonObject {
                packageKey->currentModuleOrPackage,
                nameKey->name
            };
        }
    } else {
        throw AssertionError("Type of flags ``type.flags`` not implemented");
    }
}
