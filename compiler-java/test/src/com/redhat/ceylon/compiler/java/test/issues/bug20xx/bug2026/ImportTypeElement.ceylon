shared class ImportTypeElement(name, nestedImports)
        extends ImportElement() {
    
    shared actual Identifier name;
    shared actual ImportElements? nestedImports;
    
    shared actual [Identifier, ImportElements=] children;
    if (exists nestedImports) {
        children = [name, nestedImports];
    } else {
        children = [name];
    }
}
