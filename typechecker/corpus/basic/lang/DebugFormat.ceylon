shared extension class DebugFormat(Object? obj) 
        satisfies Format {
    shared actual String formatted {
        if (obj exists) {
            return obj.string
        }
        else {
            return "null"
        }
    }
}