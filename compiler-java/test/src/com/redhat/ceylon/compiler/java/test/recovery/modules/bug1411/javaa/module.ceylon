"Runtime tests that require java modules"
native("jvm")
module javaa "1" {
    import "org.apache.camel.camel-core" "2.9.4";
}