/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyClassAlias;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyInterfaceAlias;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyTypeAlias;
import com.redhat.ceylon.compiler.loader.model.LazyValue;

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
