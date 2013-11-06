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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <Return,Args> Callable<Return> flatten(
            @Ignore final TypeDescriptor $reifiedReturn,
            @Ignore final TypeDescriptor $reifiedArgs, 
        @Name("tupleFunction")
        @TypeInfo("ceylon.language::Callable<Return,ceylon.language::Tuple<Args,Args,ceylon.language::Empty>>")
        final Callable<? extends Return> tupleFunction) {
        
        return new AbstractCallable<Return>($reifiedReturn, $reifiedArgs, null, (short)-1) {

            @Override
            public Return $call$() {
                return tupleFunction.$call$(empty_.get_());
            }

            @Override
            public Return $call$(java.lang.Object arg0) {
                TypeDescriptor[] typeArguments = ((TypeDescriptor.Class)$reifiedArgs).getTypeArguments();
                return tupleFunction.$call$(new Tuple(typeArguments[0], typeArguments[1], typeArguments[2], arg0, empty_.get_()));
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1) {
                TypeDescriptor[] typeArguments0 = ((TypeDescriptor.Class)$reifiedArgs).getTypeArguments();
                TypeDescriptor[] typeArguments1 = ((TypeDescriptor.Class)typeArguments0[2]).getTypeArguments();
                return tupleFunction.$call$(new Tuple(typeArguments0[0], typeArguments0[1], typeArguments0[2], 
                                           arg0, new Tuple(typeArguments1[0], typeArguments1[1], typeArguments1[2], 
                                                           arg1, empty_.get_())));
            }

            @Override
            public Return $call$(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2) {
                TypeDescriptor[] typeArguments0 = ((TypeDescriptor.Class)$reifiedArgs).getTypeArguments();
                TypeDescriptor[] typeArguments1 = ((TypeDescriptor.Class)typeArguments0[2]).getTypeArguments();
                TypeDescriptor[] typeArguments2 = ((TypeDescriptor.Class)typeArguments1[2]).getTypeArguments();
                return tupleFunction.$call$(new Tuple(typeArguments0[0], typeArguments0[1], typeArguments0[2], 
                                           arg0, new Tuple(typeArguments1[0], typeArguments1[1], typeArguments1[2], 
                                                           arg1, new Tuple(typeArguments2[0], typeArguments2[1], typeArguments2[2], 
                                                                           arg2, empty_.get_()))));
            }

            @Override
            public Return $call$(java.lang.Object... args) {
                Sequential t = empty_.get_();
                TypeDescriptor[][] typeArguments = new TypeDescriptor[args.length][];
                typeArguments[0] = ((TypeDescriptor.Class)$reifiedArgs).getTypeArguments();
                for (int i=1; i<args.length; i++) {
                    typeArguments[i] = ((TypeDescriptor.Class)typeArguments[i-1][2]).getTypeArguments();
                }
                for (int i=args.length-1;i>=0;i--) {
                    t = new Tuple(typeArguments[i][0], typeArguments[i][1], typeArguments[i][2], args[i], t);
                }
                return tupleFunction.$call$(t);
            }
            
            @Override
            public java.lang.String toString() {
                return $getType$().toString();
            }
        
        };
    }
}
