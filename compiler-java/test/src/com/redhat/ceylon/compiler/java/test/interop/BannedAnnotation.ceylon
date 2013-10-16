import java.lang{jdeprecated=deprecated, override}
import java.lang.annotation{
    target, 
    retention, 
    RetentionPolicy{
        runtime=RUNTIME
    },
    ElementType{
        type=TYPE
    }
}

target([type])
retention(runtime)
final annotation class BannedAnnotation() satisfies Annotation<BannedAnnotation> {
    jdeprecated
    override
    void m() {
    }
}