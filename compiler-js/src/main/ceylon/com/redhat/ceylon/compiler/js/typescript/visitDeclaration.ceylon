import tsc {
    ...
}
import ceylon.json {
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
                    nameKey->name
                });
            writer.writeLine("ex$.``name`` = (function(``name``) { return function(){ return ``name``; }; })(exports.``name``);"); // TS emits a variable, Ceylon expects a function – TODO #6320; TODO also this overwrites the TypeScript value, so if any TypeScript code uses this value we’re screwed
            if (!const) {
                writer.writeLine("ex$.set$``name`` = function(``name``) { ex$.``name`` = function(){ return ``name``; }; };"); // TODO this is complete rubbish: we’re only overwriting the getter for Ceylon
            }
            // TODO ex$.name = exports.name feels wrong; surely tsc can do the right thing by itself?
            // TODO write metamodel stuff
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
