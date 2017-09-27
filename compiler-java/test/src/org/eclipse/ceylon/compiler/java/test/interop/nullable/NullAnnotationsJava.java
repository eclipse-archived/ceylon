package org.eclipse.ceylon.compiler.java.test.interop.nullable;

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

    @Nonnull
    public String getNotNullProperty2(){ return null; }
    @Nullable
    public String getNullableProperty2(){ return null; }

    public void setUnknownProperty(String str){}
    public void setNotNullProperty(@Nonnull String str){}
    public void setNullableProperty(@Nullable String str){}

    // non-annotated setters
    public void setNotNullProperty2(String str){}
    public void setNullableProperty2(String str){}
}