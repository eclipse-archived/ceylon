/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader;

import java.util.concurrent.Callable;

import org.eclipse.ceylon.model.loader.model.AnnotationProxyClass;
import org.eclipse.ceylon.model.loader.model.AnnotationProxyMethod;
import org.eclipse.ceylon.model.loader.model.LazyClass;
import org.eclipse.ceylon.model.loader.model.LazyClassAlias;
import org.eclipse.ceylon.model.loader.model.LazyFunction;
import org.eclipse.ceylon.model.loader.model.LazyInterface;
import org.eclipse.ceylon.model.loader.model.LazyInterfaceAlias;
import org.eclipse.ceylon.model.loader.model.LazyTypeAlias;
import org.eclipse.ceylon.model.loader.model.LazyValue;

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
    void complete(LazyFunction lazyMethod);

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

    <T> T synchronizedCall(Callable<T> action) throws Exception;

    void synchronizedRun(Runnable action);

    /**
     * Completes loading of an annotation proxy class
     */
    void complete(AnnotationProxyClass annotationProxyClass);

    /**
     * Completes loading of an annotation proxy method
     */
    void complete(AnnotationProxyMethod annotationProxyMethod);
}
