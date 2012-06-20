package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class $arrayOfSome {

    private $arrayOfSome() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>&Some<Element>")
    public static <Element> Array<Element> arrayOfSome(
    @Name("elements")
    @TypeInfo("ceylon.language.Sequence<Element>")
    final Iterable<? extends Element> elements) {
        throw new RuntimeException("Wrong version of arrayOfSome() being called, this should never happen!");
    }
    
    @Ignore
    public static <Element> Array<Element> arrayOfSome(
            final Class<Element> typeClass,
            final Iterable<? extends Element> elements) {
        return new ArrayOfSome<Element>(typeClass, elements);
    }
}
