package ceylon.language;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.FunctionalParameter;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Method
public final class flatten_ {
    
    private flatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,Args>")
    public static <Return,Args> Callable<Return> flatten(
            @Ignore final TypeDescriptor $reifiedReturn,
            @Ignore final TypeDescriptor $reifiedArgs, 
        @Name("tupleFunction")@FunctionalParameter("(tuple)")
        @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,ceylon.language::Empty>>")
        final Callable<? extends Return> tupleFunction) {
        
        return new AbstractCallable<Return>($reifiedReturn, $reifiedArgs, null, (short)-1) {

            private TypeDescriptor getElementType(java.lang.Object[] args, Sequential<?> tail) {
            	if ($reifiedArgs instanceof TypeDescriptor.Class) {
            		//optimization for common cases
            		TypeDescriptor.Class reifiedClass = (TypeDescriptor.Class) $reifiedArgs;
            		TypeDescriptor sequenceElement = reifiedClass.getSequenceElement();
            		if(sequenceElement != null)
            		    return sequenceElement;
            	}
                TypeDescriptor[] types = new TypeDescriptor[args.length + (tail != null ? 1 : 0)];
                for (int i = 0; i <args.length; i++) {
                    types[i] = getArgType(args[i]);
                }
                if(tail != null){
                    TypeDescriptor restType = getTypeDescriptor(tail);
                    TypeDescriptor elementType = 
                            Metamodel.getIteratedTypeDescriptor(restType);
                    types[args.length] = elementType;
                }
                return TypeDescriptor.union(types);
            }
            
            private TypeDescriptor getArgType(java.lang.Object arg) {
                return getTypeDescriptor(arg);
            }

			@SuppressWarnings("rawtypes")
			private Tuple tuple(java.lang.Object[] args) {
				return new Tuple(getElementType(args, null), args);
			}

			@SuppressWarnings("rawtypes")
			private Tuple tuple(java.lang.Object[] args, Sequential<?> tail) {
			    return Tuple.instance(getElementType(args, tail), args, tail);
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
            public Return $callvariadic$(Sequential<?> arg0) {
                return tupleFunction.$call$(arg0);
            }

            @Override
            public Return $callvariadic$(java.lang.Object arg0, Sequential<?> arg1) {
                return tupleFunction.$call$(tuple(new java.lang.Object[] { arg0 }, arg1));
            }

            @Override
            public Return $callvariadic$(java.lang.Object arg0, java.lang.Object arg1, Sequential<?> arg2) {
                return tupleFunction.$call$(tuple(new java.lang.Object[] { arg0, arg1}, arg2));
            }

            @Override
            public Return $callvariadic$(java.lang.Object... args) {
                // it is an array of the first args.length-1 params followed by a Sequential last
                java.lang.Object[] first = new java.lang.Object[args.length-1];
                System.arraycopy(args, 0, first, 0, args.length-1);
                return tupleFunction.$call$(tuple(first, (Sequential<?>)args[args.length-1]));
            }

            @Override
            public java.lang.String toString() {
                return $getType$().toString();
            }
        
        };
    }
}
