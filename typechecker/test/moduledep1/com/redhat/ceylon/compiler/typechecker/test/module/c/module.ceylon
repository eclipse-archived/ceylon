Module module {
    name = 'com.redhat.ceylon.compiler.typechecker.test.module.c';
    version = '1';
    doc = "Some module";
    license = 'http://www.apache.org/licenses/LICENSE-2.0.html';
    Import {
        name = 'com.redhat.ceylon.compiler.typechecker.test.module.d';
        version = '1';
        export = true;
    }
}