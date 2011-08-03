package com.redhat.ceylon.compiler.loader;

public interface ModelCompleter {

    void complete(LazyClass lazyClass);

    void completeTypeParameters(LazyClass lazyClass);

    void complete(LazyInterface lazyInterface);

    void completeTypeParameters(LazyInterface lazyInterface);

    void complete(LazyValue lazyValue);
    
    void complete(LazyMethod lazyMethod);
}
