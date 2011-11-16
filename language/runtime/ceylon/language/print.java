package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Method
public final class print {
	static final process p = process.getProcess();
    public static void print(@Name("line") @TypeInfo("ceylon.language.Object") java.lang.Object line){
    	p.writeLine(line.toString());
    }
    private print(){}
}
