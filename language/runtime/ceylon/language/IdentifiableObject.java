package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ceylon(major = 2)
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Identifiable")
public abstract class IdentifiableObject implements Identifiable {
    
}