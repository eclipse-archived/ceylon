Module module {
    name = 'com.redhat.ceylon.compiler.typechecker.test.module.testexportisolation';
    version = '1';
    doc = "Test that different versions can coexist if they are not visible at the same time";
    license = 'http://www.apache.org/licenses/LICENSE-2.0.html';
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.a';
        version = '1';
    },
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.b';
        version = '2';
    }
}