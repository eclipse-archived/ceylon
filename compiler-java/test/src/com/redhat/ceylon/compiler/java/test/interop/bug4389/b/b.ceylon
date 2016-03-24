import com.redhat.ceylon.compiler.java.test.interop.bug4389.a{
    A
}

void b() {
    A a = A();
    for (s in a) {
        
    }
    for (s in a.iterable) {
        
    }
}