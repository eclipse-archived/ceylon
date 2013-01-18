package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 4)
@Method
public final class flatten_ {
    
    private flatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,Args>")
    public static <Return,Args> Callable<Return> flatten(
        @Name("tupleFunction")
        @TypeInfo("ceylon.language::Callable<Return,Tuple<Args,Args,Empty>>")
        final Callable<? extends Return> tupleFunction) {
        
        return new Callable<Return>() {

			@Override
			public Return $call() {
				return tupleFunction.$call();
			}

			@Override
			public Return $call(java.lang.Object arg0) {
				return tupleFunction.$call(new Tuple(arg0, empty_.getEmpty$()));
			}

			@Override
			public Return $call(java.lang.Object arg0, java.lang.Object arg1) {
				return tupleFunction.$call(new Tuple(arg0, new Tuple(arg1, empty_.getEmpty$())));
			}

			@Override
			public Return $call(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
				return tupleFunction.$call(new Tuple(arg0, new Tuple(arg1, new Tuple(arg2, empty_.getEmpty$()))));
			}

			@Override
			public Return $call(java.lang.Object... args) {
				Sequential t = empty_.getEmpty$();
				for (java.lang.Object arg: args) {
					t = new Tuple(arg, t);
				}
				return tupleFunction.$call(t);
			}
        	
		};    
    }
        
}
