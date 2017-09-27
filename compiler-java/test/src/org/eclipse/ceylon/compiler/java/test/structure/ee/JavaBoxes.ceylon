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
@noanno
shared variable Float? fJavaBoxes = 2.0;
@noanno
shared variable String? sJavaBoxes = javaBoxesGeneric<String>();
@noanno
shared variable Byte? oJavaBoxes = 2.byte;
@noanno
shared variable Boolean? bJavaBoxes = null;
@noanno
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
@noanno
class JavaBoxesCCtor satisfies JavaBoxesI {
    
    shared actual Integer x;
    shared Integer? y;
    
    shared new get {
        x = 1;
        y = 2;
    }
    
    shared new foo(Integer? y) {
        x = 0;
        this.y = y;
    }
}
@noanno
abstract class JavaBoxesActualParameterSuper() {
    shared formal String? s;
}
@noanno
class JavaBoxesActualParameter(shared actual String string, 
    shared actual String s) 
        extends JavaBoxesActualParameterSuper() {
}
@noanno
class DefaultedVariableAttribute(firstName=null, lastName=null) {
    
    shared variable String? firstName;
    shared variable String? lastName;
}
