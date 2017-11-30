@noanno
class LateAndLazy() {
    shared late String lateDefinite = "";
    shared late String? lateOptional = "";
    shared late variable String lateVariableDefinite = "";
    shared late variable String? lateVariableOptional = "";
    
    shared late Integer lateDefinite2 = system.milliseconds;
    shared late Integer? lateOptional2 = system.milliseconds;
    shared late variable Integer lateVariableDefinite2 = system.milliseconds;
    shared late variable Integer? lateVariableOptional2 = system.milliseconds;
}
@noanno
shared late String lateDefinite = "";
@noanno
shared late String? lateOptional = "";
@noanno
shared late variable String lateVariableDefinite = "";
@noanno
shared late variable String? lateVariableOptional = "";
@noanno
class LateAndLazyAndStatic {
    shared static late String lateDefinite = "";
    shared static late String? lateOptional = "";
    shared static late variable String lateVariableDefinite = "";
    shared static late variable String? lateVariableOptional = "";
    shared new() {}
}
@noanno
class LateHash() {
    shared actual late Integer hash = 10;
}
