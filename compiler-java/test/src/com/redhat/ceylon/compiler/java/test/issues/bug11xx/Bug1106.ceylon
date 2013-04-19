@noanno
shared interface Bug1106_Resource {
    shared formal String path;
    shared formal String contents;

    shared class Bug1106_Stub(path, contents) satisfies Bug1106_Resource {
        shared actual String path;
        shared actual String contents;
    }
}