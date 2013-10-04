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

@Ceylon(major = 5)
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
            public Return $call() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Return $call(java.lang.Object arg0) {
                Sequential seq = (Sequential) arg0;
                int variadicParameterIndex = flatFunction.$getVariadicParameterIndex();
                switch ((int) seq.getSize()) {
                case 0:
                    // even if the function is variadic it will overload $call so we're good
                    return flatFunction.$call();
                case 1:
                    // if the first param is variadic, just pass the sequence along
                    if(variadicParameterIndex == 0)
                        return flatFunction.$call$variadic(seq);
                    return flatFunction.$call(seq.get(Integer.instance(0)));
                case 2:
                    switch(variadicParameterIndex){
                    // pass the sequence along
                    case 0: return flatFunction.$call$variadic(seq);
                    // extract the first, pass the rest
                    case 1: return flatFunction.$call$variadic(seq.get(Integer.instance(0)), 
                                                      (Sequential)seq.spanFrom(Integer.instance(1)));
                    // no variadic param, or after we run out of elements to pass
                    default:
                        return flatFunction.$call(seq.get(Integer.instance(0)), 
                                                  seq.get(Integer.instance(1)));
                    }
                case 3:
                    switch(variadicParameterIndex){
                    // pass the sequence along
                    case 0: return flatFunction.$call$variadic(seq);
                    // extract the first, pass the rest
                    case 1: return flatFunction.$call$variadic(seq.get(Integer.instance(0)), 
                                                      (Sequential)seq.spanFrom(Integer.instance(1)));
                    // extract the first and second, pass the rest
                    case 2: return flatFunction.$call$variadic(seq.get(Integer.instance(0)),
                                                      seq.get(Integer.instance(1)),
                                                      (Sequential)seq.spanFrom(Integer.instance(2)));
                    // no variadic param, or after we run out of elements to pass
                    default:
                    return flatFunction.$call(seq.get(Integer.instance(0)), 
                                              seq.get(Integer.instance(1)), 
                                              seq.get(Integer.instance(2)));
                    }
                default:
                    switch(variadicParameterIndex){
                    // pass the sequence along
                    case 0: return flatFunction.$call$variadic(seq);
                    // extract the first, pass the rest
                    case 1: return flatFunction.$call$variadic(seq.get(Integer.instance(0)), 
                                                     (Sequential)seq.spanFrom(Integer.instance(1)));
                    // extract the first and second, pass the rest
                    case 2: return flatFunction.$call$variadic(seq.get(Integer.instance(0)),
                                                      seq.get(Integer.instance(1)),
                                                      (Sequential)seq.spanFrom(Integer.instance(2)));
                    case 3: return flatFunction.$call$variadic(seq.get(Integer.instance(0)),
                                                    seq.get(Integer.instance(1)),
                                                    seq.get(Integer.instance(2)),
                                                    (Sequential)seq.spanFrom(Integer.instance(3)));
                    // no variadic param
                    case -1:
                        java.lang.Object[] args = Util.toArray(seq, new java.lang.Object[(int) seq.getSize()]);
                        return flatFunction.$call(args);
                    // we have a variadic param in there bothering us
                    default:
                        // we stuff everything before the variadic into an array
                        int beforeVariadic = (int)Math.min(seq.getSize(), variadicParameterIndex);
                        boolean needsVariadic = beforeVariadic < seq.getSize();
                        args = new java.lang.Object[beforeVariadic + (needsVariadic ? 1 : 0)];
                        Iterator iterator = seq.iterator();
                        java.lang.Object it;
                        int i=0;
                        while(i < beforeVariadic && (it = iterator.next()) != finished_.get_()){
                            args[i++] = it;
                        }
                        // add the remainder as a variadic arg if required
                        if(needsVariadic){
                            args[i] = seq.spanFrom(Integer.instance(beforeVariadic));
                            return flatFunction.$call$variadic(args);
                        }
                        return flatFunction.$call(args);
                    }
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
            

            
            public java.lang.String toString() {
                return $getType().toString();
            }
        };
    }
}
