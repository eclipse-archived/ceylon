import com.redhat.ceylon.compiler.java.test.metamodel { JavaType { StaticClass }}
import ceylon.language.meta {
    type
}

void bug1793() {
    value classDev = StaticClass(1);
    print(type(classDev));
}