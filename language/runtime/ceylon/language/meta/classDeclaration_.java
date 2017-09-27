package ceylon.language.meta;

import ceylon.language.meta.declaration.ClassDeclaration;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major=8, minor=0)
@Method
public class classDeclaration_ {
    
    @Ignore
    private classDeclaration_(){}
    
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration")
    public static ClassDeclaration classDeclaration(
            @TypeInfo("ceylon.language::Anything")
            @Name("instance")
            java.lang.Object instance) {
        return (ClassDeclaration)Metamodel.getOrCreateMetamodel(instance.getClass());
    }
}
