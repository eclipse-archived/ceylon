@error;
Module module {
    name = 'com.redhat.ceylon.compiler.typechecker.test.module.testdiffversionexport';
    version = '1';
    doc = "Test imports from different versions fails if visible in the same scope (via export)";
    license = 'http://www.apache.org/licenses/LICENSE-2.0.html';
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.c';
        version = '1';
    },
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.d';
        version = '2';
    }
}