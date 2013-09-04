package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.reflect.Field;

import ceylon.language.model.Annotated$impl;
import ceylon.language.model.declaration.Import$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeImport 
    implements ceylon.language.model.declaration.Import,
        ceylon.language.model.Annotated, AnnotationBearing,
        ReifiedType {
    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeImport.class);
    
    private final FreeModule module;
    
    private final ModuleImport moduleImport;
    
    public FreeImport(FreeModule module, ModuleImport moduleImport) {
        this.module = module;
        this.moduleImport = moduleImport;
    }

    @Override
    @Ignore
    public Import$impl $ceylon$language$model$declaration$Import$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public Annotated$impl $ceylon$language$model$Annotated$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        final Field field = getField();
        return field != null ? field.getAnnotations() : AnnotationBearing.NONE;
    }

    private Field getField() {
        Class<?> javaClass = Metamodel.getJavaClass(this.module.declaration);
        String fieldName = getName().replace('.', '$');
        final Field field;
        try {
            field = javaClass.getField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return moduleImport.getModule().getNameAsString();
    }
    
    @Override
    @TypeInfo("ceylon.language::String")
    public String getVersion() {
        return moduleImport.getModule().getVersion();
    }

    @Override
    public boolean getShared() {
        return moduleImport.isExport();
    }

    @Override
    public boolean getOptional() {
        return moduleImport.isOptional();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
