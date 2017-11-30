import check { check }

shared void test6719() {
    import nesting {
        obj6719 { xval = xval }
    }
    check("OK!!!" == xval, "#6719");
}

interface I6719 {
    shared default String xval => "OK!!!";
}
object obj6719 satisfies I6719 {}
