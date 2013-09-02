package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ignore
public interface $LanguageAccess {

    /**
     * Create an {@code ArraySequence} backed by the given elements 
     * ({@code first} and {@code length})
     * of the given {@code array}, 
     * taking a copy or not depending on {@code copy}.
     */
    public <Element> ArraySequence<Element> newArraySequence(
            TypeDescriptor $reifiedElement, 
            Element[] array, long first, long length, 
            boolean copy);
}
