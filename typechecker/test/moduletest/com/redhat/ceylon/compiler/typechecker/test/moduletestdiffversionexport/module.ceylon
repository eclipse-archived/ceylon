$error 
"Test imports from different versions fails if visible in the same scope (via export)"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.ceylon.compiler.typechecker.test.moduletestdiffversionexport "1" {
    import org.eclipse.ceylon.compiler.typechecker.test.modulec "1";
    import org.eclipse.ceylon.compiler.typechecker.test.moduled "2";
}