Module module {
    name = 'com.redhat.sample.circularity.c2';
    version = '0.1';
    doc = "Test circularity in module dependency";
    license = 'http://www.gnu.org/licenses/lgpl.html';
    Import {
        //@error missing dependency
        @error name = 'com.redhat.sample.circularity.c1';
        version = '0.3';
    }
}