import tsc {
    ...
}
import ceylon.json {
    JsonObject
}

JsonObject convertTypeForModel(Type type) {
    switch (type.flags)
    case (TypeFlags.String) {
        return JsonObject {
            "md"->"$",
            "pk"->"$",
            "nm"->"String"
        };
    }
    else {
        throw AssertionError("Type of flags ``type.flags`` not implemented");
    }
}
