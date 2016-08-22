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
    if (hasTypeFlag(type.flags, TypeFlags.String)) {
        return stringTypeForModel;
    } else if (hasTypeFlag(type.flags, TypeFlags.Number)) {
        if (parameterType) {
            return floatOrIntegerTypeForModel;
        } else {
            return floatTypeForModel;
        }
    } else if (hasTypeFlag(type.flags, TypeFlags.Boolean)) {
        return booleanTypeForModel;
    } else if (hasTypeFlag(type.flags, TypeFlags.Void) || hasTypeFlag(type.flags, TypeFlags.Any)) {
        return anythingTypeForModel;
    } else if (hasTypeFlag(type.flags, TypeFlags.Reference) || hasTypeFlag(type.flags, TypeFlags.Interface)) {
        // flags also contain e. g. TypeFlags.Class – I think we can ignore that
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
