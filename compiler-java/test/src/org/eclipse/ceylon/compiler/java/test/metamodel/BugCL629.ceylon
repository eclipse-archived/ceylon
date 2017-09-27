object bug629 {
    shared String version = `bug629`.declaration.containingModule.version;
}

shared void run629() {
    print(bug629.version);
}