@noanno
class JavaBoxes(init) {
    shared String? init;
    shared variable Integer? i = 2;
    shared variable Float? f = 2.0;
    shared variable String? s= javaBoxesGeneric<String>();
    shared variable Byte? o = 2.byte;
    shared variable Boolean? b = null;
}
@noanno
class JavaBoxesStatic {
    shared static variable Integer? i = 2;
    shared new () {}
}
@noanno
T|String? javaBoxesGeneric<T>() { return null; }
@noanno
shared variable Integer? iJavaBoxes = 2;
shared variable Float? fJavaBoxes = 2.0;
shared variable String? sJavaBoxes = javaBoxesGeneric<String>();
shared variable Byte? oJavaBoxes = 2.byte;
shared variable Boolean? bJavaBoxes = null;

class JavaBoxesP(shared Integer? x=1){
}
@noanno
interface JavaBoxesI {
    shared formal Integer? x;
}
@noanno
class JavaBoxesC(x) satisfies JavaBoxesI {
    shared actual variable Integer x;
}