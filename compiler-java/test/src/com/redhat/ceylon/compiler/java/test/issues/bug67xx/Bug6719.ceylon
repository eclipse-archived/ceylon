shared void bug6719() {
    import com.redhat.ceylon.compiler.java.test.issues.bug67xx {
        bug6719obj { xval = xval }
    }
    print(xval);
}

interface Bug6719 {
    shared default String xval => "x";
}
object bug6719obj satisfies Bug6719 {}