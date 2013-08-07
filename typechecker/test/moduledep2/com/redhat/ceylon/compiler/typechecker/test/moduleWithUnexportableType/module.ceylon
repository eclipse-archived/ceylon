"Some module which exposes imported types from an imported unexported module"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.ceylon.compiler.typechecker.test.moduleWithUnexportableType "1" {
    import com.redhat.ceylon.compiler.typechecker.test.moduleWithExportedType "1";
}