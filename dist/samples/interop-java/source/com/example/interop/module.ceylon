"The classic Hello World module with Java interop"
by("Stéphane Épardaud")
license("http://www.apache.org/licenses/LICENSE-2.0")
native("jvm")
module com.example.interop "1.0" {
    import java.base "7";
}
