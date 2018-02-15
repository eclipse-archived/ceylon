$error 
"Test that multiple imports from different versions at the same level fail"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.ceylon.compiler.typechecker.test.moduletestdiffversion "1" {
    import org.eclipse.ceylon.compiler.typechecker.test.modulec "1";
    $error import org.eclipse.ceylon.compiler.typechecker.test.modulec "2";
}