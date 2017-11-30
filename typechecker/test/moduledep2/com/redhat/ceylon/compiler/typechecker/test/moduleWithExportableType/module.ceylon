"Some module which exposes imported types from an imported exported module"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.ceylon.compiler.typechecker.test.moduleWithExportableType "1" {
    shared import org.eclipse.ceylon.compiler.typechecker.test.moduleWithExportedType "1";
}