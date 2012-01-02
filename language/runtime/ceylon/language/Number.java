package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@SatisfiedTypes({"ceylon.language.Equality"})
public interface Number {
    
    @TypeInfo(value="ceylon.language.Integer")
    public long getInteger();
    @TypeInfo(value="ceylon.language.Float")
    public double getFloat();
    
    public boolean getPositive();
    public boolean getNegative();
    
    @TypeInfo(value="ceylon.language.Integer")
    public long getSign();
    
    public Number getMagnitude();    
    public Number getFractionalPart();
    public Number getWholePart();
    
}
