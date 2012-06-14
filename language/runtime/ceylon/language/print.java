package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Method
public final class print {
	static final process p = process.getProcess();
    public static void print(@Name("line") @TypeInfo("ceylon.language.Object") java.lang.Object line){
    	p.writeLine(line.toString());
    }
    private print(){}
}
