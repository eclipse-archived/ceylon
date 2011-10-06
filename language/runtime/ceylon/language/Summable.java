package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Summable<Other extends Summable<Other>> {
    public Other plus(@Name("number") Other number);
}
