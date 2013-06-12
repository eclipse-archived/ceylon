"Test circularity in module dependency"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.sample.circularity.c1 '0.2' {
    import com.redhat.sample.circularity.c2 '0.1';
}