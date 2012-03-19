package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
public interface Number {
    
    public long getInteger();
    public double getFloat();
    
    public boolean getPositive();
    public boolean getNegative();
    
    public long getSign();
    
    public Number getMagnitude();    
    public Number getFractionalPart();
    public Number getWholePart();
    
}
