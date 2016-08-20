import tsc {
    ...
}
import ceylon.json {
    JsonObject
}

JsonObject convertTypeForModel(Type type) {
    switch (type.flags)
    case (TypeFlags.String) {
        return stringTypeForModel;
    }
    else {
        throw AssertionError("Type of flags ``type.flags`` not implemented");
    }
}
