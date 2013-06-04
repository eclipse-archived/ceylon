import ceylon.language.metamodel.declaration { Module }

shared native object modules {
    shared native Module[] list;
    
    shared native Module? find(String name, String version);
    
    // FIXME: add load/unload
}