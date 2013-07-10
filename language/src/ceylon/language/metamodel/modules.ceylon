import ceylon.language.metamodel.declaration { Module }

shared native object modules {
    shared native Module[] list;
    
    shared native Module? find(String name, String version);
    
    // FIXME: can we really not have a default module?
    shared native Module? default;
    
    // FIXME: add load/unload
}