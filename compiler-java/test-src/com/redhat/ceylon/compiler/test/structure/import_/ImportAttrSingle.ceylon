import com.redhat.ceylon.compiler.test.structure.import_.pkg{f1}

@nomodel
class ImportAttrSingle() {
    void m() {
        Boolean b = f1;
    }
}