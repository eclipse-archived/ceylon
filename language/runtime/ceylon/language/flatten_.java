package ceylon.language;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Method
public final class flatten_ {
    
    private flatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,Args>")
    public static <Return,Args> Callable<Return> flatten(
            @Ignore final TypeDescriptor $reifiedReturn,
            @Ignore final TypeDescriptor $reifiedArgs, 
        @Name("tupleFunction")
        @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,ceylon.language::Empty>>")
        final Callable<? extends Return> tupleFunction) {
        
        return new AbstractCallable<Return>($reifiedReturn, $reifiedArgs, null, (short)-1) {

            private TypeDescriptor getElementType(java.lang.Object[] args) {
            	if ($reifiedArgs instanceof TypeDescriptor.Class) {
            		//optimization for common cases
            		TypeDescriptor.Class reifiedClass = (TypeDescriptor.Class) $reifiedArgs;
            		Class<?> clazz = reifiedClass.getClass();
					if (clazz.equals(Tuple.class) ||
            			clazz.equals(Sequence.class) ||
            			clazz.equals(Sequential.class)) {
						return reifiedClass.getTypeArguments()[0];
            		}
					else if (clazz.equals(Empty.class)) {
						return TypeDescriptor.NothingType;
					}
            	}
                TypeDescriptor[] types = new TypeDescriptor[args.length];
                for (int i = 0; i <args.length; i++) {
                    types[i] = getArgType(args[i]);
                }
                return TypeDescriptor.union(types);
            }
            
            private TypeDescriptor getArgType(java.lang.Object arg) {
                return getTypeDescriptor(arg);
            }

			@SuppressWarnings("rawtypes")
			private Tuple tuple(java.lang.Object[] args) {
				return new Tuple(getElementType(args), args);
			}

            @Override
            public Return $call$() {
                return tupleFunction.$call$(empty_.get_());
            }

            @Override
            public Return $call$(java.lang.Object arg0) {
            	return tupleFunction.$call$(tuple(new java.lang.Object[] { arg0 }));
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1) {
            	return tupleFunction.$call$(tuple(new java.lang.Object[] { arg0, arg1 }));
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
            	return tupleFunction.$call$(tuple(new java.lang.Object[] { arg0, arg1, arg2 }));
            }

            @Override
            public Return $call$(java.lang.Object... args) {
                return tupleFunction.$call$(tuple(args));
            }
            
            @Override
            public java.lang.String toString() {
                return $getType$().toString();
            }
        
        };
    }
}
