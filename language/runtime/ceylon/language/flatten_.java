package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Method
public final class flatten_ {
    
    private flatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,Args>")
    public static <Return,Args> Callable<Return> flatten(
            @Ignore TypeDescriptor $reifiedReturn,
            @Ignore TypeDescriptor $reifiedArgs, 
        @Name("tupleFunction")
        @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,ceylon.language::Empty>>")
        final Callable<? extends Return> tupleFunction) {
        
        return new Callable<Return>() {

			@Override
			public Return $call() {
				return tupleFunction.$call();
			}

			@Override
			public Return $call(java.lang.Object arg0) {
			    // FIXME: implement reified
				return tupleFunction.$call(new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, arg0, empty_.getEmpty$()));
			}

			@Override
			public Return $call(java.lang.Object arg0, java.lang.Object arg1) {
                // FIXME: implement reified
				return tupleFunction.$call(new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, 
				                           arg0, new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, 
				                                           arg1, empty_.getEmpty$())));
			}

			@Override
			public Return $call(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
                // FIXME: implement reified
				return tupleFunction.$call(new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, 
				                           arg0, new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, 
				                                           arg1, new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, 
				                                                           arg2, empty_.getEmpty$()))));
			}

			@Override
			public Return $call(java.lang.Object... args) {
				Sequential t = empty_.getEmpty$();
				for (int i=args.length-1;i>=0;i--) {
				    // FIXME: implement reified
					t = new Tuple(TypeDescriptor.NothingType, TypeDescriptor.NothingType, TypeDescriptor.NothingType, args[i], t);
				}
				return tupleFunction.$call(t);
			}

            @Override
            public short $getVariadicParameterIndex() {
                return -1;
            }
        	
		};    
    }
        
}
