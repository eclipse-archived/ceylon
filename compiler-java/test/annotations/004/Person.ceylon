doc "Represents a person"
class Person(String first, String last, Language language) {
    // FIXME: It should be possible simply to write
    // package String firstName = firstName;
    // but due to the way fields are scoped that doesn't work.
    package String firstName = first;
    package String lastName = last;
    package Language lang = language;
}
