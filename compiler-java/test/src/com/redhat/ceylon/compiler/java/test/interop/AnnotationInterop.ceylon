import javax.annotation{generated, 
    postConstruct, 
    preDestroy, 
    resource,
    Resource {
        AuthenticationType{
            application=\iAPPLICATION
        }
    }
}
import java.lang {
    JString=String,
    JBoolean=Boolean,
    Thread {
        State {
            runnable=\iRUNNABLE,
            neww=\iNEW,
            blocked=\iBLOCKED
        }
    }
}
import java.util.jar {
    JarFile
}
@nomodel
generated{
    \ivalue={"Some", "code generator name"};
}
javaAnnotationPrimitives {
    b=true;
    o=Byte(8);
    s=16;
    i=32;
    l=64;
    f=32.0;
    d=64.0;
    str="";
    
    ba={true};
    oa={Byte(8)};
    sa={16};
    ia={32};
    la={64};
    fa={32.0};
    da={64.0};
    stra={""};
}
javaAnnotationClass {
    clas=`class String`;
    classRaw=`class JString`;
    classWithBound=`class Exception`;
    classExact=`class JString`;
    classes={`class JString`, `class JBoolean`, `interface Sequential`};
    classesRaw={`class JString`, `class JBoolean`, `interface Sequential`};
    classesWithBound={`class Exception`, `class AssertionError`};
    classesExact={`class JString`};
}
javaAnnotationEnum {
    threadState=neww;
    threadStates={runnable, blocked};
}
javaAnnotationAnnotation {
    annotation=javaAnnotationEnum{
        threadState=neww;
        threadStates={runnable, blocked};
    };
    annotations={javaAnnotationEnum{
        threadState=neww;
        threadStates={runnable, blocked};
    }};
}
javaAnnotationAcronym
javaAnnotationLowercase
javaAnnotationDefaultTarget__TYPE
javaAnnotationDefaultTarget__CONSTRUCTOR
javaAnnotationOnEveryTarget__TYPE
javaAnnotationOnEveryTarget__CONSTRUCTOR
class AnnotationInterop() {
    
    resource{
        name="tom";
        authenticationType=application;
        type=`AnnotationInterop`;
    }
    javaAnnotationDefaultTarget__FIELD
    javaAnnotationDefaultTarget__GETTER
    javaAnnotationDefaultTarget__SETTER
    javaAnnotationOnEveryTarget__FIELD
    javaAnnotationOnEveryTarget__GETTER
    javaAnnotationOnEveryTarget__SETTER
    shared variable String tom = "";
    
    javaAnnotationDefaultTarget
    javaAnnotationOnEveryTarget
    postConstruct
    shared void afterConstrution() {}
    
    preDestroy
    shared void beforeDestruction() {}
    
    shared void method(javaAnnotationDefaultTarget__PARAMETER
                       javaAnnotationOnEveryTarget__PARAMETER 
                       Integer param){
        javaAnnotationDefaultTarget__LOCAL_VARIABLE
        javaAnnotationOnEveryTarget__LOCAL_VARIABLE
        anno 
        Integer var = 2;
    }
}

javaAnnotationDefaultTarget__ANNOTATION_TYPE
javaAnnotationOnEveryTarget__ANNOTATION_TYPE
shared final annotation class Anno() satisfies OptionalAnnotation<Anno>{}
shared annotation Anno anno() => Anno();

class CPAnnoTest2 {
    generated({"by you"})
    shared new (generated({"by me"}) String x) { }
    shared new other(generated({"by me"}) String x) { }
}

generated({JarFile.\iMANIFEST_NAME})
void bug2103() {}

class C {
    javaAnnotationFieldTarget
    javaAnnotationMethodTarget
    javaAnnotationFieldMethodTarget
    shared String s="";
    
    //javaAnnotationFieldTarget__FIELD
    shared new val {
        
    }
    
    @error:"annotated program element does not satisfy annotation constraint: 'ValueConstructorDeclaration' is not assignable to 'ValueDeclaration'"
    javaAnnotationFieldTarget
    shared new val2 {
        
    }
    
    
}
