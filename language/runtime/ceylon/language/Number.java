package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public interface Number {
    
    @Annotations(@Annotation("formal"))
    public boolean getPositive();

    @Annotations(@Annotation("formal"))
    public boolean getNegative();

    @Annotations(@Annotation("formal"))
    public double getFloat();

    @Annotations(@Annotation("formal"))
    public long getInteger();
    
    @Annotations(@Annotation("formal"))
    public Number getMagnitude();    
    
    @Annotations(@Annotation("formal"))
    public long getSign();
    
    @Annotations(@Annotation("formal"))
    public Number getFractionalPart();

    @Annotations(@Annotation("formal"))
    public Number getWholePart();
}
