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

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Defaulted;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.Transient;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(extendsType = "ceylon.language::Basic")
@SharedAnnotation$annotation$
@AbstractAnnotation$annotation$
@SealedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public abstract class Throwable extends java.lang.Object 
        implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Throwable.class);

    @Ignore
    private final java.lang.String description;
    
    public Throwable(
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
        //super(description==null ? null : description.toString(), cause);
        this.description = description.value;
    }
    
    @Ignore
    public Throwable(String description) {
        this(description, $default$cause(description));
    }
    
    @Ignore
    public Throwable() {
        this($default$description());
    }
        
    @TypeInfo("ceylon.language::Throwable?")
    @Nullable
    public final java.lang.Throwable getCause() {
        return null;//super.getCause();
    }
    
    @TypeInfo("ceylon.language::String")
    @Transient
    @NonNull
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

    @TypeInfo("ceylon.language::String")
    @Transient
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    //@Override
    public final void printStackTrace() {
        //super.printStackTrace();
    }

    @Ignore
    public static String $default$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $default$cause(String description){
        return null;
    }
    
    @TypeInfo("ceylon.language::Throwable[]")
    @NonNull
    public final Sequential<? extends java.lang.Throwable> getSuppressed() {
        return null;
    }
    
    public final void addSuppressed(
            @Name("suppressed")
            @TypeInfo("ceylon.language::Throwable")
            @NonNull
            java.lang.Throwable suppressed) {}
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
