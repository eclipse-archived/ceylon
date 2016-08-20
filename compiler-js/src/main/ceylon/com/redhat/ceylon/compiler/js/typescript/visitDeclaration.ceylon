import tsc {
    ...
}
import ceylon.json {
    JsonArray,
    JsonObject
}

void visitDeclaration(
    "The current container to which declarations are added."
    JsonObject container,
    "The writer for the JS file"
    Sha1Writer writer,
    Program program)(Node node) {
    value typechecker = program.getTypeChecker();
    switch (node.kind)
    // actually handled cases
    case (SyntaxKind.VariableDeclaration) {
        try {
            Identifier id;
            dynamic {
                id = eval("(function(node){return node.name})")(node); // TODO necessary to dress the name as Identifier (could be BindingPattern). do this properly
            }
            assert (is VariableDeclaration vdecl = node);
            Boolean const;
            dynamic {
                const = eval("(function(vdecl, constflags){return (vdecl.parent.flags & constflags) != 0})")(vdecl, NodeFlags.Const); // TODO can we write this in Ceylon?
            }
            String name = id.text;
            value type = typechecker.getTypeAtLocation(vdecl);
            container.put(name, JsonObject {
                    typeKey -> convertTypeForModel(type),
                    packedAnnotationsKey -> packAnnotations {
                        shared = true;
                        variable = !const;
                    },
                    metaTypeKey->attributeMetaType,
                    nameKey->name,
                    dynamicKey->1
                });
            // TODO write metamodel stuff
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    case (SyntaxKind.FunctionDeclaration | SyntaxKind.MethodDeclaration | SyntaxKind.MethodSignature) {
        try {
            assert (is FunctionDeclaration fdecl = node);
            Identifier id;
            dynamic {
                id = eval("(function(x){return x.name})")(node); // TODO should not be necessary once the backend can handle optional members
            }
            value name = id.text;
            value type = typechecker.getReturnTypeOfSignature(typechecker.getSignatureFromDeclaration(fdecl));
            container.put(name, JsonObject {
                    metaTypeKey->methodMetaType,
                    nameKey->name,
                    // TODO type parameters
                    typeKey -> convertTypeForModel(type),
                    parametersKey -> JsonArray { convertParameterListForModel(fdecl.parameters, typechecker) }, // wrap again because it’s one of potentially many parameter lists
                    flagsKey->0, // void flag has no effect and deferred flag doesn’t seem to be used in JS backend
                    packedAnnotationsKey -> packAnnotations {
                        shared = true;
                        formal = node.kind == SyntaxKind.MethodSignature;
                    }
                });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    // descend
    case (SyntaxKind.VariableStatement) {
        try {
            Boolean exported;
            dynamic {
                // TODO can we write this in Ceylon?
                exported = eval("(function(flags, exportFlag) { return (flags & exportFlag) != 0; })")(node.flags, NodeFlags.Export);
            }
            if (exported) {
                assert (is VariableStatement decl = node);
                forEachChild(decl.declarationList, visitDeclaration(container, writer, program));
            }
        } catch (Throwable t) { t.printStackTrace(); }
    }
    case (SyntaxKind.VariableDeclarationList) {
        forEachChild(node, visitDeclaration(container, writer, program));
    }
    // ignore
    case (SyntaxKind.EndOfFileToken) {
    }
    else {
        process.writeErrorLine("Unknown declaration kind ``node.kind``");
    }
}
