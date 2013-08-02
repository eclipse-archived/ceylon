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
    Thread {
        State {
            runnable=\iRUNNABLE,
            new=\iNEW,
            blocked=\iBLOCKED
        }
    }
}

@nomodel
generated{
    \ivalue={"Some", "code generator name"};
}
javaAnnotationPrimitives {
    b=true;
    o=8;
    s=16;
    i=32;
    l=64;
    f=32.0;
    d=64.0;
    str="";
    
    ba={true};
    oa={8};
    sa={16};
    ia={32};
    la={64};
    fa={32.0};
    da={64.0};
    stra={""};
}
javaAnnotationClass {
    clas=`String`;
    classRaw=`String`;
    classWithBound=`Exception`;
    classExact=`String`;
    classes={`String`, `Boolean`};
    classesRaw={`String`, `Boolean`};
    classesWithBound={`Exception`, `AssertionException`};
    classesExact={`String`};
}
javaAnnotationEnum {
    threadState=new;
    threadStates={runnable, blocked};
}
javaAnnotationAnnotation {
    annotation=javaAnnotationEnum{
        threadState=new;
        threadStates={runnable, blocked};
    };
    annotations={javaAnnotationEnum{
        threadState=new;
        threadStates={runnable, blocked};
    }};
}
class AnnotationInterop() {
    
    resource{
        name="tom";
        authenticationType=application;
        type=`AnnotationInterop`;
    }
    shared variable String tom = "";
    
    postConstruct
    shared void afterConstrution() {}
    
    preDestroy
    shared void beforeDestruction() {}
}