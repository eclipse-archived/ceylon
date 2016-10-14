import ceylon.language.meta {
    type
}
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

void undress(Object a) {
    dynamic {
        eval("(function(a){delete a.getT$all;delete a.$$;})")(a);
    }
}

T make<T>(Object t) {
    if (type(t) == `Anything`) {
        undress(t);
    }
    assert (is T t);
    return t;
}

void visitDeclaration(
    "The current container to which declarations are added."
    JsonObject container,
    "The writer for the JS file"
    Sha1Writer writer,
    Program program)(Node node) {
    value typechecker = program.getDiagnosticsProducingTypeChecker();
    switch (node.kind)
    // actually handled cases
    case (SyntaxKind.\iVariableDeclaration | SyntaxKind.\iPropertyDeclaration | SyntaxKind.\iPropertySignature) {
        try {
            Identifier id;
            dynamic {
                id = eval("(function(node){return node.name})")(node); // TODO necessary to dress the name as Identifier (could be BindingPattern). do this properly
            }
            undress(node); // TODO why is the undress before the assert necessary?
            assert (is VariableDeclaration vdecl = node);
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
            makeExitFail();
        }
    }
    case (SyntaxKind.\iFunctionDeclaration | SyntaxKind.\iMethodDeclaration | SyntaxKind.\iMethodSignature) {
        try {
            undress(node); // TODO why is the undress before the assert necessary?
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
                        formal = node.kind == SyntaxKind.\iMethodSignature;
                    }
                });
        } catch (Throwable t) {
            t.printStackTrace();
            makeExitFail();
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
                    constructor = make<ConstructorDeclaration>(member);
                }
                case (SyntaxKind.\iPropertyDeclaration) {
                    properties.add(make<PropertyDeclaration>(member));
                }
                case (SyntaxKind.\iMethodDeclaration) {
                    methods.add(make<MethodDeclaration>(member));
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
            makeExitFail();
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
                    properties.add(make<PropertyDeclaration>(member));
                }
                case (SyntaxKind.\iMethodDeclaration | SyntaxKind.\iMethodSignature) {
                    methods.add(make<MethodDeclaration>(member));
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
            makeExitFail();
        }
    }
    case (SyntaxKind.\iEnumDeclaration) {
        try {
            assert (is EnumDeclaration edecl = node);
            Identifier id;
            dynamic {
                id = eval("(function(x){return x.name})")(node); // TODO should not be necessary once the backend can handle optional members
            }
            value name = id.text;
            Boolean const = isConstEnumDeclaration(edecl);
            JsonArray caseTypes = JsonArray {};
            JsonObject constructors = JsonObject {};
            value mapEnumName = mapEnumNames();
            for (member in edecl.members) {
                Identifier mid;
                dynamic {
                    mid = eval("(function(x){return x.name})")(member); // TODO handle other name kinds
                }
                value memberName = mid.text;
                value mappedName = mapEnumName(memberName);
                value tsenum = const then typechecker.getEmitResolver().getConstantValue(member).string else memberName;
                caseTypes.add(JsonObject {
                        packageKey->currentModuleOrPackage,
                        nameKey->"``name``.``mappedName``"
                    });
                constructors.put(mappedName, JsonObject {
                    packedAnnotationsKey->packAnnotations {
                        shared = true;
                    },
                    nameKey->mappedName,
                    tsenumKey->tsenum
                });
            }
            JsonObject enum = JsonObject {
                metaTypeKey->classMetaType,
                nameKey->name,
                dynamicKey->1,
                packedAnnotationsKey->packAnnotations {
                    shared = true;
                },
                caseTypesKey->caseTypes,
                constructorsKey->constructors
            };
            container.put(name, enum);
            if (const) {
                writer.writeLine("function ``name``(){throw Exception(\"``name`` is a TypeScript enum and has no default constructor.\");}"); // TODO include module name
            } else {
                writer.writeLine("``name``=ex$.``name``;");
            }
            writer.writeLine("function ``name``$$c($){
                              $init$``name``();
                              if($===undefined)$=new ``name``.$$;
                              return $;
                              }");
            // TODO individual members?
            // TODO $crtmm$?
            if (const) {
                writer.writeLine("ex$.``name``=``name``;");
            }
            writer.writeLine("function $init$``name``(){
                              if(``name``.$$===undefined){
                              m$1.initTypeProto(``name``,'``name``',m$1.Basic);
                              ``name``.$$.$tsenum=true;
                              }
                              return ``name``;
                              }
                              ex$.$init$``name``=$init$``name``;
                              $init$``name``();"); // TODO include module name in type name for initTypeProto; m$1.atr$()?
        } catch (Throwable t) {
            t.printStackTrace();
            makeExitFail();
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
        } catch (Throwable t) {
            t.printStackTrace();
            makeExitFail();
        }
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
