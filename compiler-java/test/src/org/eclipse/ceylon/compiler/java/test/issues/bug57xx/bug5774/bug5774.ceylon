import ceylon.interop.java {
    javaClass
}

import com.google.inject {
    inject,
    AbstractModule
}

interface Bar {}

class BarImpl() satisfies Bar {}

inject class Foo(Bar bar) {
    
}

object fooBarModule extends AbstractModule() {
    
    shared actual void configure() {
        bind(javaClass<Bar>()).to(javaClass<BarImpl>());
    }
    
}
