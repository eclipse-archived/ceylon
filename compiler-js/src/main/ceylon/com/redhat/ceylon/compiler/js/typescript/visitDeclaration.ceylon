import tsc {
    ...
}
import ceylon.json {
    JsonArray,
    JsonObject
}
import ceylon.collection {
    ArrayList,
    MutableList
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
    case (SyntaxKind.VariableDeclaration | SyntaxKind.PropertyDeclaration | SyntaxKind.PropertySignature) {
        try {
            Identifier id;
            VariableDeclaration vdecl;
            dynamic {
                id = eval("(function(node){return node.name})")(node); // TODO necessary to dress the name as Identifier (could be BindingPattern). do this properly
                vdecl = eval("(function(node){return node})")(node);
            }
            //assert (is VariableDeclaration vdecl = node); // TODO use assert
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
            //assert (is FunctionDeclaration fdecl = node);
            FunctionDeclaration fdecl;
            dynamic { fdecl = eval("(function(x){return x;})")(node); }
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
    case (SyntaxKind.ClassDeclaration) {
        try {
            assert (is ClassDeclaration cdecl = node);
            Identifier id;
            dynamic {
                id = eval("(function(x){return x.name})")(node); // TODO should not be necessary once the backend can handle optional members
            }
            value name = id.text;
            MutableList<PropertyDeclaration> properties = ArrayList<PropertyDeclaration>(cdecl.members.size);
            MutableList<MethodDeclaration> methods = ArrayList<MethodDeclaration>(cdecl.members.size);
            variable ConstructorDeclaration? constructor = null;
            for (member in cdecl.members) {
                switch (member.kind)
                case (SyntaxKind.Constructor) {
                    "Class can only have one constructor"
                    assert (!constructor exists);
                    ConstructorDeclaration consdecl;
                    dynamic {
                        consdecl = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is ConstructorDeclaration consdecl = member); // TODO use assert
                    constructor = consdecl;
                }
                case (SyntaxKind.PropertyDeclaration) {
                    PropertyDeclaration property;
                    dynamic {
                        property = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is PropertyDeclaration property = member); // TODO use assert
                    properties.add(property);
                }
                case (SyntaxKind.MethodDeclaration) {
                    MethodDeclaration method;
                    dynamic {
                        method = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is MethodDeclaration method = member); // TODO use assesrt
                    methods.add(method);
                }
                // TODO index signature
                else {
                    process.writeErrorLine("Unknown class member kind ``member.kind``");
                }
            }
            JsonObject klass = JsonObject {
                metaTypeKey->classMetaType,
                nameKey->name,
                dynamicKey->1,
                packedAnnotationsKey -> packAnnotations {
                    shared = true;
                }
            };
            if (!properties.empty) {
                JsonObject attributes = JsonObject {};
                properties.collect(visitDeclaration(attributes, writer, program));
                klass.put(attributesKey, attributes);
            }
            if (!methods.empty) {
                JsonObject methds = JsonObject {};
                methods.collect(visitDeclaration(methds, writer, program));
                klass.put(methodsKey, methds);
            }
            if (exists cons = constructor) {
                klass.put(parametersKey, convertParameterListForModel(cons.parameters, typechecker));
            }
            container.put(name, klass);
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
