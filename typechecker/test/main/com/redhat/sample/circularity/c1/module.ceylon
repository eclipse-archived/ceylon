"Test circularity in module dependency"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.sample.circularity.c1 "0.2" {
    import org.eclipse.sample.circularity.c2 "0.1";
}