import ceylon.json {
    JsonArray,
    JsonObject
}
import tsc {
    ...
}

JsonArray convertParameterListForModel(NodeArray<ParameterDeclaration> list, TypeChecker typechecker) {
    variable Integer generatedNameCounter = 1;
    String generateName() {
        while (generatedNameCounter > 0) {
            value name = "param_`` generatedNameCounter++ ``";
            for (param in list) {
                if (param.name.kind == SyntaxKind.Identifier) {
                    assert (is Identifier id = param.name);
                    if (id.text == name) {
                        break;
                    }
                }
            } else {
                return name;
            }
        }
        throw Exception("Cannot generate unique parameter name");
    }
    String parameterName(ParameterDeclaration param) {
        switch (param.name.kind)
        case (SyntaxKind.Identifier) {
            assert (is Identifier id = param.name);
            return id.text;
        }
        case (SyntaxKind.ObjectBindingPattern | SyntaxKind.ArrayBindingPattern) {
            return generateName();
        }
    }
    Boolean isDefaulted(ParameterDeclaration param) {
        dynamic {
            return eval("(function(x){return (x.questionToken||x.initializer||false)&&true})")(param); // TODO shouldn’t be necessary once the compiler can deal with optional members
        }
    }
    return JsonArray {
        for (param in list)
            JsonObject {
                nameKey -> parameterName(param),
                metaTypeKey->parameterMetaType,
                typeKey -> convertTypeForModel(typechecker.getTypeAtLocation(param)),
                packedAnnotationsKey -> packAnnotations(),
                if (isDefaulted(param))
                    defaultKey->1
            }
    };
    // TODO variadic / rest parameters
}
