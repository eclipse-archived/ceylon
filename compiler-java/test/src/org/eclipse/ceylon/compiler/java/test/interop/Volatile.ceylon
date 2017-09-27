import java.lang{volatile}

@noanno
class Volatile(s, 
    shared volatile variable String u) {
    shared volatile variable String s;
    shared volatile variable String t=s;
}
// toplevel values are already thread-safe