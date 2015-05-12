package com.redhat.ceylon.model.loader;

import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.loader.model.LazyClassAlias;
import com.redhat.ceylon.model.loader.model.LazyInterface;
import com.redhat.ceylon.model.loader.model.LazyInterfaceAlias;
import com.redhat.ceylon.model.loader.model.LazyMethod;
import com.redhat.ceylon.model.loader.model.LazyTypeAlias;
import com.redhat.ceylon.model.loader.model.LazyValue;

/**
 * Represents something which can complete a model if needed. This is used because we load declarations lazily,
 * so we only fully load them when needed.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface ModelCompleter {

    /**
     * Completes loading of a class
     */
    void complete(LazyClass lazyClass);

    /**
     * Completes loading of a class's type parameters only
     */
    void completeTypeParameters(LazyClass lazyClass);

    /**
     * Completes loading of an interface
     */
    void complete(LazyInterface lazyInterface);

    /**
     * Completes loading of an interface's type parameters only
     */
    void completeTypeParameters(LazyInterface lazyInterface);

    /**
     * Completes loading of a toplevel attribute
     */
    void complete(LazyValue lazyValue);
    
    /**
     * Completes loading of a toplevel method
     */
    void complete(LazyMethod lazyMethod);

    /**
     * Completes loading of a lazy class alias
     */
    void complete(LazyClassAlias lazyClassAlias);

    /**
     * Completes loading of a lazy class alias's type parameters only
     */
    void completeTypeParameters(LazyClassAlias lazyClassAlias);

    /**
     * Completes loading of a lazy interface alias
     */
    void complete(LazyInterfaceAlias lazyInterfaceAlias);

    /**
     * Completes loading of a lazy interface alias's type parameters only
     */
    void completeTypeParameters(LazyInterfaceAlias lazyInterfaceAlias);

    /**
     * Completes loading of a lazy type alias
     */
    void complete(LazyTypeAlias lazyTypeAlias);

    /**
     * Completes loading of a lazy type alias's type parameters only
     */
    void completeTypeParameters(LazyTypeAlias lazyTypeAlias);

    /**
     * Returns a lock we can use for thread-safety
     */
    Object getLock();
}
