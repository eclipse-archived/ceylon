"Test multi source file compilation"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module com.redhat.sample.multisource "0.2" {
    shared import ceylon.language "0.6.1";
    @error import non.existent.something "1.0";
}
