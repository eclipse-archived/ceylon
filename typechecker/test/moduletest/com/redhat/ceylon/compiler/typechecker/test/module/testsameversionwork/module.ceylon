Module module {
    name = 'com.redhat.ceylon.compiler.typechecker.test.module.testsameversionwork';
    version = '1';
    doc = "Test that multiple imports from the same version even when visible to each other works";
    license = 'http://www.apache.org/licenses/LICENSE-2.0.html';
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.c';
        version = '1';
    },
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.d';
        version = '1';
    }
}