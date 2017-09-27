"Test extracting imported version as a constant"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.ceylon.compiler.typechecker.test.moduletestconstant "1" {
    value version = "1";
    $error value num = 1;
    $error value num = version;
    import org.eclipse.ceylon.compiler.typechecker.test.modulec version;
    import org.eclipse.ceylon.compiler.typechecker.test.moduled version;
}