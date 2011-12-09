package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
	@TypeParameter(value="Return", variance=Variance.OUT),
	@TypeParameter(value="CallableArgument")
})
public interface Callable<Return,Argument> {

}
