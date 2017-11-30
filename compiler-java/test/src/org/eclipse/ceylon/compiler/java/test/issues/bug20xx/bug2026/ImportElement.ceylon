shared abstract class ImportElement()
        extends Node() {
    
    shared formal Identifier name;
    shared formal ImportElements? nestedImports;
    
    shared actual formal [Identifier, ImportElements=] children;
}
