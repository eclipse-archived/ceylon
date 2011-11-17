package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Category {
    public boolean contains(@Name("element") Object element);
    
    public boolean containsEvery(@Name("elements") Iterable<? extends Object> elements);

    public boolean containsAny(@Name("elements") Iterable<? extends Object> elements);
}
