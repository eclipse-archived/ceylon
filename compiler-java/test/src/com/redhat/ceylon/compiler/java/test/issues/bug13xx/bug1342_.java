package com.redhat.ceylon.compiler.java.test.issues.bug13xx;

final class bug1342_ {
    
    static void bug134X(final ceylon.language.meta.model.Class<? extends ceylon.language.SharedAnnotation, ? super ceylon.language.Empty> s, 
            final ceylon.language.meta.declaration.ValueDeclaration a) {
        ceylon.language.SharedAnnotation sa = (ceylon.language.SharedAnnotation)(java.lang.Object)
                ceylon.language.meta.optionalAnnotation_.optionalAnnotation(null, null, (ceylon.language.meta.model.Class)s, a);
    }
}