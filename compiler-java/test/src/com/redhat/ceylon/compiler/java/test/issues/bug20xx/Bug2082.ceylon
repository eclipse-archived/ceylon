@noanno
interface Bug2082 of eBug2082 | fBug2082 {
    shared Boolean isNeat(String val) => false;
}
@noanno
object eBug2082 satisfies Bug2082 {
    shared Boolean isValid(String val) => false;
}
@noanno
object fBug2082 satisfies Bug2082 {}
@noanno
Boolean isValid(String val, Bug2082 type, Bug2082? type2) {
    if (is \IeBug2082 type) {
        type.isNeat(val);
        return type.isValid(val);
        
    }
    if (is \IeBug2082 type2) {
        type2.isNeat(val);
        return type2.isValid(val);
        
    }
    return true;
}