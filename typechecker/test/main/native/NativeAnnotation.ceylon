final native("jvm") annotation class Ann() 
        satisfies OptionalAnnotation<Ann> {}
native("jvm") annotation Ann ann() => Ann();

$error ann
native void annotated() {}

ann
native("jvm") void annotated() {}

$error ann
native("js") void annotated() {}