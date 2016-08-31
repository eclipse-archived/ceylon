package com.redhat.ceylon.compiler.java.test.issues.bug63xx;

interface Bug6323ParserDefinition {
    abstract Bug6323CeylonFile createFile(Bug6323FileViewProvider vp);
}

class Bug6323CeylonFile{
    
    Bug6323CeylonFile(Bug6323FileViewProvider vp) {}
}

interface Bug6323FileViewProvider {
}
