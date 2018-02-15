"Some module which exposes imported types from an imported unexported module"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.ceylon.compiler.typechecker.test.moduleWithUnexportableType "1" {
    import org.eclipse.ceylon.compiler.typechecker.test.moduleWithExportedType "1";
}