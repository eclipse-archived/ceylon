@nomodel
class KlassWithObjectMember(String x) {
    object localobj {
    	shared String f = x; 
        shared String m() {
            return x;
        }
    }
    object capturedobj {
    	shared String f = x; 
        shared String m() {
            return x;
        }
    }
    shared object sharedobj {
    	shared String f = x; 
        shared String m() {
            return x;
        }
    }
    
    void m() {
        capturedobj.m();
    }
}