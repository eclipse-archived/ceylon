Module module {
    name = 'com.redhat.ceylon.compiler.test.structure.module.depend.c';
    version = '6.6.6';
    doc = "Bla bla.";
    authors = { "Stef FroMage" };
    license = 'http://www.gnu.org/licenses/gpl.html';
    Import {
        name = 'com.redhat.ceylon.compiler.test.structure.module.depend.a';
        version = '6.6.6';
        optional = false;
        export = false;
    },
    Import {
        name = 'com.redhat.ceylon.compiler.test.structure.module.depend.b';
        version = '6.6.6';
        optional = false;
        export = false;
    }
}