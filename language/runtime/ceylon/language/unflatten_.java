package ceylon.language;

import com.redhat.ceylon.compiler.java.Util;
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
public final class unflatten_ {
    
    private unflatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,ceylon.language::Empty>>")
    public static <Return,Args> Callable<Return> unflatten(
        @Ignore TypeDescriptor $reifiedReturn,
        @Ignore TypeDescriptor $reifiedArgs,
        @Name("flatFunction")
        @TypeInfo("ceylon.language::Callable<Return,Args>")
        final Callable<? extends Return> flatFunction) {
        
        return new AbstractCallable<Return>($reifiedReturn, $reifiedArgs, null, (short)-1) {

            @Override
            public Return $call$() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Return $call$(java.lang.Object arg0) {
                Sequential<?> seq = (Sequential<?>) arg0;
                return Util.<Return>apply(flatFunction, seq);
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Return $call$(java.lang.Object... args) {
                throw new UnsupportedOperationException();
            }
            

            
            public java.lang.String toString() {
                return $getType$().toString();
            }
        };
    }
}
