Module module {
    name = 'com.redhat.sample.multisource';
    version = '0.2';
    doc = "Test multi source file compilation";
    license = 'http://www.apache.org/licenses/LICENSE-2.0.html';
    @error
    Import {
        name = 'ceylon.language';
        version = '0.3.1';
        export = true;
    },
    Import {
        name = 'non.existent.module';
        version = '1.0';
    }
}