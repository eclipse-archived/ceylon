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
    case (SyntaxKind.\iVariableDeclaration | SyntaxKind.\iPropertyDeclaration | SyntaxKind.\iPropertySignature) {
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
                const = hasNodeFlag(eval("(function(v){return v.parent.flags})")(vdecl), NodeFlags.\iConst);
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
    case (SyntaxKind.\iFunctionDeclaration | SyntaxKind.\iMethodDeclaration | SyntaxKind.\iMethodSignature) {
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
                        formal = node.kind == SyntaxKind.\iMethodSignature;
                    }
                });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    case (SyntaxKind.\iClassDeclaration) {
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
                case (SyntaxKind.\iConstructor) {
                    "Class can only have one constructor"
                    assert (!constructor exists);
                    ConstructorDeclaration consdecl;
                    dynamic {
                        consdecl = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is ConstructorDeclaration consdecl = member); // TODO use assert
                    constructor = consdecl;
                }
                case (SyntaxKind.\iPropertyDeclaration) {
                    PropertyDeclaration property;
                    dynamic {
                        property = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is PropertyDeclaration property = member); // TODO use assert
                    properties.add(property);
                }
                case (SyntaxKind.\iMethodDeclaration) {
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
                JsonObject attributes = JsonObject { };
                properties.collect(visitDeclaration(attributes, writer, program));
                klass.put(attributesKey, attributes);
            }
            if (!methods.empty) {
                JsonObject methds = JsonObject { };
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
    case (SyntaxKind.\iInterfaceDeclaration) {
        try {
            assert (is InterfaceDeclaration idecl = node);
            Identifier id;
            dynamic {
                id = eval("(function(x){return x.name})")(node); // TODO should not be necessary once the backend can handle optional members
            }
            value name = id.text;
            MutableList<PropertyDeclaration> properties = ArrayList<PropertyDeclaration>(idecl.members.size);
            MutableList<MethodDeclaration> methods = ArrayList<MethodDeclaration>(idecl.members.size);
            for (member in idecl.members) {
                switch (member.kind)
                case (SyntaxKind.\iPropertyDeclaration | SyntaxKind.\iPropertySignature) {
                    PropertyDeclaration property;
                    dynamic {
                        property = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is PropertyDeclaration property = member); // TODO use assert
                    properties.add(property);
                }
                case (SyntaxKind.\iMethodDeclaration | SyntaxKind.\iMethodSignature) {
                    MethodDeclaration method;
                    dynamic {
                        method = eval("(function(x){x.getT$all=undefined;x.$$=undefined;return x})")(member);
                    }
                    //assert (is MethodDeclaration method = member); // TODO use assesrt
                    methods.add(method);
                }
                // TODO index signature, call signature
                else {
                    process.writeErrorLine("Unknown interface member kind ``member.kind``");
                }
            }
            JsonObject iface = JsonObject {
                metaTypeKey->interfaceMetaType,
                nameKey->name,
                dynamicKey->1,
                packedAnnotationsKey -> packAnnotations {
                    shared = true;
                }
            };
            if (!properties.empty) {
                JsonObject attributes = JsonObject { };
                properties.collect(visitDeclaration(attributes, writer, program));
                iface.put(attributesKey, attributes);
            }
            if (!methods.empty) {
                JsonObject methds = JsonObject { };
                methods.collect(visitDeclaration(methds, writer, program));
                iface.put(methodsKey, methds);
            }
            container.put(name, iface);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    // descend
    case (SyntaxKind.\iVariableStatement) {
        try {
            Boolean exported = hasNodeFlag(node.flags, NodeFlags.\iExport);
            if (exported) {
                assert (is VariableStatement decl = node);
                forEachChild(decl.declarationList, visitDeclaration(container, writer, program));
            }
        } catch (Throwable t) { t.printStackTrace(); }
    }
    case (SyntaxKind.\iVariableDeclarationList) {
        forEachChild(node, visitDeclaration(container, writer, program));
    }
    // ignore
    case (SyntaxKind.\iEndOfFileToken) {
    }
    else {
        process.writeErrorLine("Unknown declaration kind ``node.kind``");
    }
}
