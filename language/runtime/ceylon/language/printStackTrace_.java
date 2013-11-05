package ceylon.language;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Method
public final class printStackTrace_
{
    public static void printStackTrace(
            @Name("exception") final java.lang.Throwable throwable,
            @TypeInfo("ceylon.language::Callable<ceylon.language::Anything,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
            @Defaulted @Name("write") final Callable<java.lang.Object> write) {
        PrintWriter writer = new PrintWriter(new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                write.$call$(String.instance(new java.lang.String(Arrays.copyOfRange(cbuf, off, off+len))));
            }
            @Override
            public void flush() throws IOException {}
            @Override
            public void close() throws IOException {}
        });
        throwable.printStackTrace(writer);
        writer.flush();
    }
    
    @Ignore
    public static Callable<java.lang.Object> printStackTrace$writeLine() {
        return new AbstractCallable<java.lang.Object>(Anything.$TypeDescriptor$, 
                TypeDescriptor.klass(Tuple.class, String.$TypeDescriptor$, String.$TypeDescriptor$, Empty.$TypeDescriptor$),
                "write", (short)-1) {
            @Override
            public java.lang.Object $call$(java.lang.Object arg) {
                System.err.print(arg);
                return null;
            }
        };
    }
    
    private printStackTrace_(){}
}
