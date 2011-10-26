@nomodel
class KlassWithObjectMember(String x) {
    object localobj {
        shared String m() {
            return x;
        }
    }
    object capturedobj {
        shared String m() {
            return x;
        }
    }
    shared object sharedobj {
        shared String m() {
            return x;
        }
    }
    
    void m() {
        capturedobj.m();
    }
}