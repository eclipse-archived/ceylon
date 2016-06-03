package ceylon.language.meta;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getReflectedAnnotationClass;
import ceylon.language.Annotation;
import ceylon.language.AssertionError;
import ceylon.language.OptionalAnnotation;
import ceylon.language.Sequence;
import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major = 8)
@Method
public final class annotations_ {
    
    private annotations_() {}
    
    // TODO @Shared$annotation
    @SuppressWarnings({ "unchecked" })
    @Annotations({@com.redhat.ceylon.compiler.java.metadata.Annotation("shared")})
    @TypeInfo("Values")
    @TypeParameters({
        @TypeParameter(value = "Value", 
                satisfies = {"ceylon.language::ConstrainedAnnotation<Value,Values,ProgramElement>"}), 
        @TypeParameter(value = "Values"), 
        @TypeParameter(value = "ProgramElement", 
                satisfies = {"ceylon.language::Annotated"}, 
                variance=Variance.IN)
    })
    public static <Value extends java.lang.annotation.Annotation, Values, ProgramElement extends ceylon.language.Annotated>Values annotations(@com.redhat.ceylon.compiler.java.metadata.Ignore
            final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $reified$Value, @com.redhat.ceylon.compiler.java.metadata.Ignore
            final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $reified$Values, @com.redhat.ceylon.compiler.java.metadata.Ignore
            final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $reified$ProgramElement, @com.redhat.ceylon.compiler.java.metadata.Name("annotationType")
            @com.redhat.ceylon.compiler.java.metadata.TypeInfo(
                    value = "ceylon.language.meta.model::Class<ceylon.language::ConstrainedAnnotation<Value,Values,ProgramElement>,ceylon.language::Nothing>",
                    erased = true)
            final ceylon.language.meta.model.Class annotationType, @com.redhat.ceylon.compiler.java.metadata.Name("programElement")
            @com.redhat.ceylon.compiler.java.metadata.TypeInfo(
                    value = "ProgramElement",
                    erased = true)
            final ProgramElement programElement) {
        Sequential<? extends Annotation> results = 
                Metamodel.annotations($reified$Value, programElement);
        boolean optional = OptionalAnnotation.class
        		.isAssignableFrom(getReflectedAnnotationClass(annotationType));
		if (optional) {
            if (results.getSize() > 1L) {
                throw new AssertionError("optional annotation occurs more than once");
            }
            Object singleResult = 
                    results instanceof Sequence ? 
                            ((Sequence<?>) results).getFirst() : 
                            null;
            return (Values) singleResult;   
        }
        else {
            return (Values) results;
        }
    }
}