package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@Method
public final class unflatten_ {
    
    private unflatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,Empty>>")
    public static <Return,Args> Callable<Return> unflatten(
        @Ignore TypeDescriptor $reifiedReturn,
        @Ignore TypeDescriptor $reifiedArgs,
        @Name("flatFunction")
        @TypeInfo("ceylon.language::Callable<Return,Args>")
        final Callable<? extends Return> flatFunction) {
        
        return new Callable<Return>() {

			@Override
			public Return $call() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Return $call(java.lang.Object arg0) {
				Sequential seq = (Sequential) arg0;
				switch ((int) seq.getSize()) {
				case 0:
					return flatFunction.$call();
				case 1:
					return flatFunction.$call(seq.get(Integer.instance(0)));
				case 2:
					return flatFunction.$call(seq.get(Integer.instance(0)), 
							seq.get(Integer.instance(1)));
				case 3:
					return flatFunction.$call(seq.get(Integer.instance(0)), 
							seq.get(Integer.instance(1)), 
							seq.get(Integer.instance(2)));
				default:
					java.lang.Object[] args = new java.lang.Object[(int) seq.getSize()];
					for (int i=0; i<seq.getSize(); i++) {
						args[i] = seq.get(Integer.instance(i));
					}
					return flatFunction.$call(args);
				}
			}

			@Override
			public Return $call(java.lang.Object arg0, java.lang.Object arg1) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Return $call(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Return $call(java.lang.Object... args) {
				throw new UnsupportedOperationException();
			}
        	
		};    
    }
        
}
