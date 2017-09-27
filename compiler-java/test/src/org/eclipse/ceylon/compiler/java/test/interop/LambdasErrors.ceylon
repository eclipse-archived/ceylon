import java.util.\ifunction {
    Consumer, IntConsumer, IntSupplier
}
import java.util {
    ArrayList
}
import java.lang { CharSequence, ShortArray, FloatArray }
import org.eclipse.ceylon.compiler.java.test.interop { LambdasJava { consumerStatic }}

class Sub3() satisfies InterfaceWithCoercedMembers {
    shared actual void m(CharSequence cs, IntSupplier l){}
    shared actual void m(String s, Integer() l){}
}
