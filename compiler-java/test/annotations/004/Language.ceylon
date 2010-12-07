doc "Represents a language"
doc "Just a dummy class until we have enumerated lists of instances"
class Language(String name) {
    public String value = name;
    public mutable String hello;

    if (name == "English") {
        hello := "Hello";
    } else {
        if (name == "French") {
            hello := "Bonjour";
        }
    }
}
