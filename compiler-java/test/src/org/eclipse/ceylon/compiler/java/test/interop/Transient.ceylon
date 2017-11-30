import java.lang{transient}

@noanno
class Transient(s, 
    shared transient String u) {
    shared transient String s;
    shared transient String t=s;
}
@noanno
object transientObject {
    shared transient variable String? s = null;
}