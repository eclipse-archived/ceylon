"Test that different versions can coexist if they are not visible at the same time"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.ceylon.compiler.typechecker.test.moduletestexportisolation '1' {
    import com.redhat.ceylon.compiler.typechecker.test.modulea '1';
    import com.redhat.ceylon.compiler.typechecker.test.moduleb '2';
}