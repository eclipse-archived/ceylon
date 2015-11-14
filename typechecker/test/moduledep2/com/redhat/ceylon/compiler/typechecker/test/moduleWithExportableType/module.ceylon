"Some module which exposes imported types from an imported exported module"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.ceylon.compiler.typechecker.test.moduleWithExportableType "1" {
    shared import com.redhat.ceylon.compiler.typechecker.test.moduleWithExportedType "1";
}