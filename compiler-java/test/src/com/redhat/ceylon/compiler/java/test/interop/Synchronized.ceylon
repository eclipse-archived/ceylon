import java.lang{synchronized}

@noanno
class Synchronized(s, shared variable synchronized String u, shared synchronized void f()) {
    shared synchronized String s;
    shared variable synchronized String t=s;
    shared synchronized void m(String s=this.s) {
    }
}
@noanno
class SynchronizedStatic {
    shared variable synchronized static String s = "";
    shared synchronized static String getter => "";
    assign getter {}
    shared synchronized static void m(String s="") {
    }
    shared new() {}
}
// toplevel values are already thread-safe
@noanno
shared synchronized String synchronizedGetter => "";
assign synchronizedGetter {}
@noanno
shared synchronized void synchronizedFunction(String s="") {
}