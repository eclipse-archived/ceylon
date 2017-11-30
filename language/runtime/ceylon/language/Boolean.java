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

import org.eclipse.ceylon.compiler.java.metadata.CaseTypes;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.ValueType;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(constructors = true)
@CaseTypes({"ceylon.language::true", "ceylon.language::false"})
@SatisfiedTypes({})
@ValueType
@SharedAnnotation$annotation$
@AbstractAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public abstract class Boolean 
        implements ReifiedType, java.io.Serializable {

    private static final long serialVersionUID = -2696784743732343464L;
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Boolean.class);
    
    @Ignore
    public static Boolean instance(boolean value) {
        return value ? true_.get_() : false_.get_();
    }

    @Ignore
    abstract public boolean booleanValue();
    
    @SharedAnnotation$annotation$
    @StaticAnnotation$annotation$
    @TypeInfo("ceylon.language::Boolean|ceylon.language::ParseException")
    public static java.lang.Object parse(
            @Name("string") java.lang.String string) {
        if (string==null) {
            throw new AssertionError("null argument in native code");
        }
        switch (string) {
        case "true": return instance(true);
        case "false": return instance(false);
        }
        return new ParseException("illegal format for Boolean");
    }

    @SharedAnnotation$annotation$
    public Boolean(){}

    @Ignore
    public static java.lang.String toString(boolean value) {
        return value ? "true" : "false";
    }
    
    @Ignore
    public static boolean equals(boolean value, java.lang.Object that) {
        if (that instanceof Boolean) {
            return value == ((Boolean) that).booleanValue();
        }
        else {
            return false;
        }
    }

    @Override
    @Ignore
    public int hashCode() {
        return hashCode(booleanValue());
    }

    @Ignore
    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @Ignore
    public static Boolean valueOf(java.lang.String string) {
        return instance(java.lang.Boolean.parseBoolean(string));
    }
    
}
