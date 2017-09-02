"Test extracting imported version as a constant"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.ceylon.compiler.typechecker.test.moduletestconstant "1" {
    value version = "1";
    $error value num = 1;
    $error value num = version;
    import com.redhat.ceylon.compiler.typechecker.test.modulec version;
    import com.redhat.ceylon.compiler.typechecker.test.moduled version;
}