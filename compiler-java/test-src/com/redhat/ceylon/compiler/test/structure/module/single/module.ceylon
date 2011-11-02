Module module {
    name = 'com.redhat.ceylon.compiler.test.structure.module.single';
    version = '6.6.6';
    doc = "Bla bla.";
    authors = { "Stef FroMage" };
    license = 'http://www.gnu.org/licenses/gpl.html';
    Import {
        name = 'foo';
        version = '1.2';
        optional = false;
        export = false;
    },
    Import {
        name = 'bar';
        version = '3.4';
        optional = true;
        export = true;
    }
}