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
            dynamic {
                id = eval("(function(node){return node.name})")(node); // TODO necessary to dress the name as Identifier (could be BindingPattern). do this properly
                vdecl = eval("(function(x){return x})")(node); // TODO use assert instead; #6307
            }
            value name = id.text;
            value type = typechecker.getTypeAtLocation(vdecl);
            container.put(name, JsonObject {
                    "$t" -> convertTypeForModel(type),
                    "pa" -> packAnnotations { shared = true; }, // TODO variable? depends on if const or not
                    "mt"->"a",
                    "nm"->name
                });
            writer.writeLine("ex$.``name`` = (function(``name``) { return function(){ return ``name``; }; })(exports.``name``);"); // TS emits a variable, Ceylon expects a function – TODO #6320
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
