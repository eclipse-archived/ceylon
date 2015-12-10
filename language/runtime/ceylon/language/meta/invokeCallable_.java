package ceylon.language.meta;

import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.model.typechecker.model.Type;

import ceylon.language.Callable;
import ceylon.language.Sequential;
import ceylon.language.meta.model.IncompatibleTypeException;

@Ceylon(major = 8)
@Method
public class invokeCallable_ {
    
    private invokeCallable_() {}
    
    @TypeInfo("ceylon.language::Anything")
    public static Object invokeCallable(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Anything,ceylon.language::Nothing>")
            @Name("callable") 
            Callable<? extends Object> callable,
            @TypeInfo("ceylon.language::Sequential<ceylon.language::Anything>")
            @Name("arguments") 
            Sequential<? extends Object> arguments) {
        int size = (int)arguments.getSize();
        
        Type parametersTuple = Metamodel.getCallableParametersTupleType(callable);
        Type argumentsTuple = Metamodel.getArgumentsTupleType(arguments);
        
        if (!argumentsTuple.isSubtypeOf(parametersTuple)) {
            throw new IncompatibleTypeException("arguments " + argumentsTuple + " not assignable to parameters " + parametersTuple);
        }
        
        switch (size) {
        case 0:
            return callable.$call$();
        case 1:
            return callable.$call$(arguments.getFromFirst(0L));
        case 2:
            return callable.$call$(arguments.getFromFirst(0L), arguments.getFromFirst(1L));
        case 3:
            return callable.$call$(arguments.getFromFirst(0L), arguments.getFromFirst(1L), arguments.getFromFirst(2L));
        default:
            Object[] args = new Object[size];
            for (int ii = 0; ii < size; ii++) {
                args[ii] = arguments.getFromFirst((long)ii);
            }
            return callable.$call$(args);
        }
    }
}
