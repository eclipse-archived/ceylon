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
            VariableDeclaration vdecl;
            Boolean const;
            dynamic {
                id = eval("(function(node){return node.name})")(node); // TODO necessary to dress the name as Identifier (could be BindingPattern). do this properly
                vdecl = eval("(function(x){return x})")(node); // TODO use assert instead; #6307
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
            FunctionDeclaration fdecl;
            Identifier id;
            TypeNode typeNode;
            dynamic {
                fdecl = eval("(function(x){return x})")(node); // TODO use assert instead; #6307
                id = eval("(function(x){return x.name})")(node); // TODO should not be necessary once the backend can handle optional members
                typeNode = eval("(function(x){return x.type})")(node); // TODO ditto
            }
            value name = id.text;
            value type = typechecker.getTypeAtLocation(typeNode);
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
                VariableStatement decl;
                dynamic {
                    decl = eval("(function(x){return x})")(node); // TODO use assert instead; #6307
                }
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
