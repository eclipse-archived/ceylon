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

import java.io.ObjectStreamException;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Object;
import org.eclipse.ceylon.compiler.java.metadata.Transient;
import org.eclipse.ceylon.compiler.java.metadata.ValueType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8) 
@Object
@Class(extendsType = "ceylon.language::Boolean")
@ValueType
@SharedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public final class true_ extends Boolean {

    private static final long serialVersionUID = -6256274272803665251L;
    
    @Ignore
    java.lang.Object readResolve() 
            throws ObjectStreamException {
        return value;
    }
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ 
            = TypeDescriptor.klass(true_.class);

    private final static true_ value = new true_();

    @Ignore
    public static true_ get_(){
        return value;
    }

    @Override
    @Ignore
    public boolean booleanValue() {
        return true;
    }

    @Override
    @Transient
    public final int hashCode() {
        return hashCode(booleanValue());
    }

    @Ignore
    public static int hashCode(boolean value) {
        return 1231;
    }
    
    @Override
    @Transient
    public java.lang.String toString() {
        return "true";
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$(){
        return $TypeDescriptor$;
    }
}
