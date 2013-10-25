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
final annotation class BannedAnnotation() satisfies OptionalAnnotation<BannedAnnotation, Annotated> {
    jdeprecated
    override
    void m() {
    }
}