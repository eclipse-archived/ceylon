import org.eclipse.ceylon.compiler.java.test.model {
    Bug6584AnnotationInClass {
        myAnnotation
    },
    Bug6584AnnotationInInterface {
        myOtherAnnotation
    }
}

myAnnotation({"a", "b", "c"})
shared class Bug6584() {

    myOtherAnnotation {
        \ivalue = {"a"};
        position = 1337;
    }
    shared void fun() {}
}