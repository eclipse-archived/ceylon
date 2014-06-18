package ceylon.language.meta;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getReflectedAnnotationClass;
import ceylon.language.Annotated;
import ceylon.language.Annotation;
import ceylon.language.AssertionError;
import ceylon.language.ConstrainedAnnotation;
import ceylon.language.OptionalAnnotation;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.meta.model.Class;

import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
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
    public static <Value extends ConstrainedAnnotation
                          <? extends Value, 
                           ? extends Values, 
                           ? super ProgramElement>, 
                   Values, 
                   ProgramElement extends Annotated>
    Values annotations(
            @Ignore
            final TypeDescriptor $reifiedValue, 
            @Ignore
            final TypeDescriptor $reifiedValues, 
            @Ignore
            final TypeDescriptor $reifiedProgramElement, 
            @Name("annotationType")
            @TypeInfo("ceylon.language.meta.model::Class<ceylon.language::ConstrainedAnnotation<Value,Values,ProgramElement>,ceylon.language::Nothing>")
            final Class<? extends ConstrainedAnnotation
                    <? extends Value, 
                     ? extends Values, 
                     ? super ProgramElement>,
                     ?> 
            annotationType, 
            @Name("programElement")
            @TypeInfo("ProgramElement")
            final ProgramElement programElement) {
        Sequential<? extends Annotation> results = 
                Metamodel.annotations($reifiedValue, programElement);
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