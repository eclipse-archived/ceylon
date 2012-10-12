package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({
    @TypeParameter(value = "First", variance = Variance.OUT),
    @TypeParameter(value = "Rest", variance = Variance.OUT, 
            satisfies="ceylon.language.TupleOrUnit")
 })
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.TupleOrUnit")
public class Tuple<First, Rest> implements TupleOrUnit {
	
	private final First first;
	private final Rest rest;
	
	public Tuple(@Name("first") First first, 
			     @Name("rest") Rest rest) {
		this.first = first;
		this.rest = rest;
	}
	
	public First getFirst() {
		return first;
	}
	
	public Rest getRest() {
		return rest;
	}

	@Override
	public int hashCode() {
	    return (31 + first.hashCode()) * 31 + rest.hashCode();
	}
	
	@Override
	public boolean equals(@Name("that") java.lang.Object that) {
		if (that instanceof Tuple) {
			Tuple entry = (Tuple) that;
			return this.first.equals(entry.first) && 
					this.rest.equals(entry.rest);
		} 
		else {
			return false;
		}
	}
	
	@Override
	public java.lang.String toString() {
		return "(" + first.toString() + "," + 
				rest.toString().substring(1);
	}
}
