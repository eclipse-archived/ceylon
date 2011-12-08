@error; //circularity
Module module {
    name = 'com.redhat.sample.circularity.c1';
    version = '0.2';
    doc = "Test circularity in module dependency";
    license = 'http://www.apache.org/licenses/LICENSE-2.0.html';
    Import {
        name = 'com.redhat.sample.circularity.c2';
        version = '0.1';
    }
}