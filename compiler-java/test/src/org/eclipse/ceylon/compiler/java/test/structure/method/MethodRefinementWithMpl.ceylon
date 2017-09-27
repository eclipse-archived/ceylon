@noanno
interface MethodRefinementWithMpl {
    shared formal String two(String s)(Integer i);
    shared formal String three(String s)(Integer i)(Boolean b);
}
@noanno
class MethodRefinementWithMplSub() satisfies MethodRefinementWithMpl {
    two(String s) => (Integer i) => s+i.string;
    three(String s) => (Integer i) => (Boolean b) => s+i.string+b.string;
}