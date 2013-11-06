package ceylon.language;

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

            private TypeDescriptor getElementType() {
                return ((TypeDescriptor.Class) $reifiedArgs).getTypeArguments()[0];
            }

            @Override
            public Return $call$() {
                return tupleFunction.$call$(empty_.get_());
            }

            @Override
            public Return $call$(java.lang.Object arg0) {
                return tupleFunction.$call$(new Tuple(getElementType(), new java.lang.Object[] { arg0 }));
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1) {
                return tupleFunction.$call$(new Tuple(getElementType(), new java.lang.Object[] { arg0, arg1 }));
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
                return tupleFunction.$call$(new Tuple(getElementType(), new java.lang.Object[] { arg0, arg1, arg2 }));
            }

            @Override
            public Return $call$(java.lang.Object... args) {
                return tupleFunction.$call$(new Tuple(getElementType(), args));
            }
            
            @Override
            public java.lang.String toString() {
                return $getType$().toString();
            }
        
        };
    }
}
