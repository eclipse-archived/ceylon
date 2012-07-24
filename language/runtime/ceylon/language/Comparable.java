package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 2)
@TypeParameters(@TypeParameter(value = "Other", variance = Variance.IN,
    		satisfies="ceylon.language.Comparable<Other>"))
@CaseTypes(of = "Other")
public interface Comparable<Other extends Comparable<? super Other>> {
    
    @Annotations(@Annotation("formal"))
    public Comparison compare(@Name("other") Other other);
    
    /*public boolean largerThan(@Name("other") Other other); 
    
    public boolean smallerThan(@Name("other") Other other);
    
    public boolean asLargeAs(@Name("other") Other other);
    
    public boolean asSmallAs(@Name("other") Other other);
    
    @Ignore
    public static final class Comparable$impl {
        public static <Other extends Comparable<Other>> boolean largerThan(Comparable<Other> $this, Other other) {
            return $this.compare(other)==larger.getLarger();
        }

        public static <Other extends Comparable<Other>> boolean smallerThan(Comparable<Other> $this, Other other) {
            return $this.compare(other)==smaller.getSmaller();
        }

        public static <Other extends Comparable<Other>> boolean asLargeAs(Comparable<Other> $this, Other other) {
            return $this.compare(other)!=smaller.getSmaller();
        }

        public static <Other extends Comparable<Other>> boolean asSmallAs(Comparable<Other> $this, Other other) {
            return $this.compare(other)!=larger.getLarger();
        }

    }
     */
}
