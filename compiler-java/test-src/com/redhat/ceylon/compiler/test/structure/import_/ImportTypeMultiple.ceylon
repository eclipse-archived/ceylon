import com.redhat.ceylon.compiler.test.structure.import_.pkg{C1, C2}

@nomodel
class ImportTypeMultiple() {
    void m() {
        C1();
        C2();
    }
}