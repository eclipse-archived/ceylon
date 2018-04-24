/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 8)
@TypeParameters({
    @TypeParameter(value="Return", variance=Variance.OUT),
    @TypeParameter(value="Arguments", satisfies="ceylon.language::Sequential<ceylon.language::Anything>", variance=Variance.IN)
})
@SharedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public interface Callable<Return> {
    
    /** 
     * Make a nullary invocation of this Callable declared with fixed arity.
     */
    @Ignore
    public Return $call$();
    
    /** 
     * Make a nullary invocation of this Callable declared with variable arity 
     * @throws UnsupportedOperationException If this callable is not variadic 
     */
    @Ignore
    public Return $callvariadic$();
    @Ignore
    public Return $callvariadic$(Sequential<?> varargs);
    
    /** Make a unary invocation of this Callable declared with fixed arity */
    @Ignore
    public Return $call$(java.lang.Object arg0);
    
    /** 
     * Make a unary invocation of this Callable declared with variable arity 
     * @throws UnsupportedOperationException If this callable is not variadic
     */
    @Ignore
    public Return $callvariadic$(java.lang.Object arg0);
    @Ignore
    public Return $callvariadic$(java.lang.Object arg0, Sequential<?> varargs);
    
    /** Make a binary invocation of this Callable declared with fixed arity */
    @Ignore
    public Return $call$(java.lang.Object arg0, java.lang.Object arg1);
    
    /** 
     * Make a binary invocation of this Callable declared with variable arity 
     * @throws UnsupportedOperationException If this callable is not variadic
     */
    @Ignore
    public Return $callvariadic$(java.lang.Object arg0, java.lang.Object arg1);
    @Ignore
    public Return $callvariadic$(java.lang.Object arg0, java.lang.Object arg1, Sequential<?> varargs);
    
    /** Make a ternary invocation of this Callable declared with fixed arity */
    @Ignore
    public Return $call$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2);
    
    /** 
     * Make a ternary invocation of this Callable declared with variable arity
     * @throws UnsupportedOperationException If this callable is not variadic 
     */
    @Ignore
    public Return $callvariadic$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2);
    @Ignore
    public Return $callvariadic$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2, Sequential<?> varargs);
    
    /** 
     * Make a fixed arity invocation of this Callable declared with fixed arity 
     * @throws UnsupportedOperationException If this callable is not variadic
     */
    @Ignore
    public Return $call$(java.lang.Object... args);
    
    /** 
     * Make a variable varity invocation of this Callable declared with variable arity 
     * @throws UnsupportedOperationException If this callable is not variadic
     */
    @Ignore
    public Return $callvariadic$(java.lang.Object... argsAndVarargs);

    /**
     * @return the index of the variadic parameter if any, -1 otherwise.
     */
    @Ignore
    public short $getVariadicParameterIndex$();

}
