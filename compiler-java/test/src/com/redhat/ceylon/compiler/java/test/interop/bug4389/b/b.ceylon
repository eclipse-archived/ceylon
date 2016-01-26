import com.redhat.ceylon.compiler.java.test.interop.bug4389.a{A}

void b() {
    for (s in A()) {
        
    }
    for (s in A().it) {
        
    }
}