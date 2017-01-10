@noanno
class LateAndLazy() {
    shared late String lateDefinite = "";
    shared late String? lateOptional = "";
    shared late variable String lateVariableDefinite = "";
    shared late variable String? lateVariableOptional = "";
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