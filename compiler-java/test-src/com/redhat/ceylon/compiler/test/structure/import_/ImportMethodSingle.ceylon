import com.redhat.ceylon.compiler.test.structure.import_.pkg{m1}

@nomodel
class ImportMethodSingle() {
    void m() {
        m1();
    }
}