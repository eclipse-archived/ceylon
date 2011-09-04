import com.redhat.ceylon.compiler.test.structure.import_.pkg{Foo=C1}

@nomodel
class ImportTypeAlias() {
    void m() {
        Foo();
    }
}