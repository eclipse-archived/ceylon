package foo.foo;

import org.eclipse.ceylon.compiler.java.metadata.Import;
import org.eclipse.ceylon.compiler.java.metadata.Module;

@Module(name = "foo.foo",
        version = "1", 
        dependencies = {
        @Import(name = "a.a", version = "1"),
        @Import(name = "b.b", version = "2", export = true),
        @Import(name = "c.c", version = "3", optional = true),
})
public class $module_ {
}
