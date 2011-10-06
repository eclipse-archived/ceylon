package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Number extends Format {
    public ceylon.language.Natural natural();
    public ceylon.language.Integer integer();
    @Name("float")
    public ceylon.language.Float toFloat();
}
