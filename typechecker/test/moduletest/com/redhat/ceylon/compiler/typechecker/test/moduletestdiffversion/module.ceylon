@error;
"Test that multiple imports from different versions at the same level fail"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.ceylon.compiler.typechecker.test.moduletestdiffversion "1" {
    import com.redhat.ceylon.compiler.typechecker.test.modulec "1";
    import com.redhat.ceylon.compiler.typechecker.test.modulec "2";
}