package ceylon.language.meta;

import ceylon.language.meta.declaration.ClassDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major=8, minor=0)
@Method
public class classDeclaration_ {
    
    @Ignore
    private classDeclaration_(){}
    
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration")
    public static ClassDeclaration classDeclaration(java.lang.Object instance) {
        return (ClassDeclaration)Metamodel.getOrCreateMetamodel(instance.getClass());
    }
}
