import ceylon.interop.java{javaClass}
javaAnnotationNoTarget__TYPE
javaAnnotationNoTarget__CONSTRUCTOR
class AnnotationInteropError() {
    
    javaAnnotationNoTarget__FIELD
    javaAnnotationNoTarget__GETTER
    javaAnnotationNoTarget__SETTER
    shared variable String tom = "";
    @error:"annotated program element does not satisfy annotation constraint: []"
    javaAnnotationNoTarget
    shared void method(javaAnnotationNoTarget__PARAMETER
                       Integer param){
        javaAnnotationNoTarget__LOCAL_VARIABLE
        Integer var = 2;
    }
}

javaAnnotationNoTarget__ANNOTATION_TYPE
javaAnnotationClass2 {
    clas=javaClass<String>();
}
shared final annotation class AnnoError() satisfies OptionalAnnotation<AnnoError>{}
