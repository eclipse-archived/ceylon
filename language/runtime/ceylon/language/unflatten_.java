package ceylon.language;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.language.AbstractCallable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.FunctionalParameter;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Method
@SharedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public final class unflatten_ {
    
    private unflatten_() {}
    
    @TypeParameters({@TypeParameter(value="Return"),
                     @TypeParameter(value="Args", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")})
    @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,ceylon.language::Empty>>")
    @FunctionalParameter("(args)")
    public static <Return,Args> Callable<Return> unflatten(
        @Ignore TypeDescriptor $reifiedReturn,
        @Ignore TypeDescriptor $reifiedArgs,
        @Name("flatFunction")
        @TypeInfo("ceylon.language::Callable<Return,Args>")
        final Callable<? extends Return> flatFunction) {
        
        return new AbstractCallable<Return>($reifiedReturn, 
                TypeDescriptor.klass(Tuple.class, $reifiedArgs, $reifiedArgs, Empty.$TypeDescriptor$), 
                null, (short)-1) {

            @Override
            public Return $call$() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Return $call$(java.lang.Object arg0) {
                Sequential<?> seq = (Sequential<?>) arg0;
                return Util.<Return>apply(flatFunction, seq, null);
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

            @Override
            public java.lang.String toString() {
                return $getType$().toString();
            }
        };
    }
}
