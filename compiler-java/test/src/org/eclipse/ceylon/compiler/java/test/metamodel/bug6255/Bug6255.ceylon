import ceylon.language.meta.declaration{FunctionDeclaration}

shared native void bug6255a();
shared native("jvm") void bug6255a() => print("a-jvm");
shared native("js") void bug6255a() => print("a-js");

shared void bug6255() {
    assert(`package`.members<FunctionDeclaration>().filter((f) => f.name == "bug6255a").size == 1);
}