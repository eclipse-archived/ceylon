import com.redhat.ceylon.compiler.java.test.issues.bug05xx.bug569 { Foo }

class Bar() satisfies Foo {
    shared actual void bar(String s) {}
}