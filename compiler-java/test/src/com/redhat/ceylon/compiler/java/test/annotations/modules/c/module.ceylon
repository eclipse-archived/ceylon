"Module C doc"
by "Tom"
license "http://www.apache.org/licenses/LICENSE-2.0.html"
tagged "test" "annotations"
deprecated "doesn't _do_ anything"
module com.redhat.ceylon.compiler.java.test.annotations.modules.c '0.1' {
    shared import com.redhat.ceylon.compiler.java.test.annotations.modules.a '0.1';
    "Optionally depend on b"
    deprecated "doesn't _do_ anything either" 
    optional import com.redhat.ceylon.compiler.java.test.annotations.modules.b '0.1';
}