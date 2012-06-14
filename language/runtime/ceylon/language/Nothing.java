package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

@Ceylon(major = 1)
@Class(extendsType="ceylon.language.Void")
@CaseTypes({"ceylon.language.null"})
public abstract class Nothing extends Void {}
