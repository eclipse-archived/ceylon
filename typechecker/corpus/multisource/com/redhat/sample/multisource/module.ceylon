Module module {
    name = 'com.redhat.sample.multisource';
    version = '0.1';
    doc = "Test multi source file compilation";
    license = 'http://www.gnu.org/licenses/lgpl.html';
    Import {
        name = 'ceylon.language';
        version = '0.1';
        export = true;
    }
}