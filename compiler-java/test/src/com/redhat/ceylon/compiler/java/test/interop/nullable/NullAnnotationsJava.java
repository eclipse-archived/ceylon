package com.redhat.ceylon.compiler.java.test.interop.nullable;

import javax.annotation.*;

public class NullAnnotationsJava {
    public String unknown;
    @Nonnull
    public String notNull;
    @Nullable
    public String nullable;
    
    public NullAnnotationsJava(String unknown, @Nonnull String notNull, @Nullable String nullable){}

    public void method(String unknown, @Nonnull String notNull, @Nullable String nullable){}
    
    public String unknown(){ return null; }
    @Nonnull
    public String notNull(){ return null; }
    @Nullable
    public String nullable(){ return null; }

    public String getUnknownProperty(){ return null; }
    @Nonnull
    public String getNotNullProperty(){ return null; }
    @Nullable
    public String getNullableProperty(){ return null; }
}