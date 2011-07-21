package com.redhat.ceylon.compiler.codegen;

public interface ModelCompleter {

    void complete(LazyClass lazyClass);

    void complete(LazyInterface lazyInterface);

    void complete(LazyValue lazyValue);
    
    void complete(LazyMethod lazyMethod);
}
