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

import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Defaulted;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(extendsType = "ceylon.language::Throwable")
@SharedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public class Exception extends RuntimeException implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Exception.class);

    private static final long serialVersionUID = -1790691559137471641L;

    @Ignore
    private final String description;
    
    public Exception(
            @TypeInfo("ceylon.language::String?")
            @Name("description")
            @Defaulted
            @Nullable
            String description,
            @TypeInfo("ceylon.language::Throwable?")
            @Name("cause")
            @Defaulted
            @Nullable
            java.lang.Throwable cause) {
        super(description==null ? null : description.toString(), cause);
        this.description = description;
    }
    
    @Ignore
    public Exception(String description) {
        this(description, $default$cause(description));
    }
    
    @Ignore
    public Exception() {
        this($default$description());
    }
        
    @Override
    @Ignore
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @Override
    @Ignore
    public java.lang.String getMessage() {
        if (description != null) {
            return description.toString();
        } 
        else if (getCause() != null 
                && getCause().getMessage() != null) {
            return getCause().getMessage();
        }
        return "";
    }

    @Override
    @Ignore
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    @Override
    @Ignore
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Ignore
    public static String $default$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $default$cause(String description){
        return null;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
