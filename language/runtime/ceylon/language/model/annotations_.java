package ceylon.language.model;

import ceylon.language.Sequence;
import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Method
public final class annotations_ {
    
    private annotations_() {
    }
    
    // TODO @Shared$annotation
    @Annotations({@com.redhat.ceylon.compiler.java.metadata.Annotation("shared")})
    @TypeInfo("Values")
    @TypeParameters({
        @TypeParameter(value = "Value", satisfies = {"ceylon.language.model::ConstrainedAnnotation<Value,Values,ProgramElement>"}), 
        @TypeParameter(value = "Values"), 
        @TypeParameter(value = "ProgramElement", satisfies = {"ceylon.language.model::Annotated"})
    })
    public static <Value extends ConstrainedAnnotation<? extends Value, ? extends Values, ? super ProgramElement>, Values, ProgramElement extends ceylon.language.model.Annotated>
    Values annotations(
            @Ignore
            final TypeDescriptor $reifiedValue, 
            @Ignore
            final TypeDescriptor $reifiedValues, 
            @Ignore
            final TypeDescriptor $reifiedProgramElement, 
            @Name("annotationType")
            @TypeInfo("ceylon.language.model::Class<ceylon.language.model::ConstrainedAnnotation<Value,Values,ProgramElement>,ceylon.language::Nothing>")
            final Class<? extends ConstrainedAnnotation<? extends Value, ? extends Values, ? super ProgramElement>,?> annotationType, 
            @Name("programElement")
            @TypeInfo("ProgramElement")
            final ProgramElement programElement) {
        Sequential<Annotation> annots = (Sequential)Metamodel.annotations($reifiedValue, programElement);
        final java.lang.Class<?> refAnnotationClass = (java.lang.Class<?>)Metamodel.getReflectedAnnotationClass(annotationType);
        if (OptionalAnnotation.class.isAssignableFrom(refAnnotationClass)) {
            if (annots.getSize() > 1L) {
                throw new RuntimeException("Assertion error");
            }
            return (Values)(annots.getEmpty() ? null : ((Sequence)annots).get(ceylon.language.Integer.instance(0)));   
        } else {
            return (Values)annots;
        }
    }
}