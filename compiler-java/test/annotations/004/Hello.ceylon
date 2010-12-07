doc "A personalized greeting"
people {
    Person ("Gavin", "King", Language("English")),
    Person ("Emmanuel", "Bernard", Language("English")),
    Person ("Pete", "Muir", Language("English")),
    Person ("Andrew", "Haley", Language("English"))
}
class Hello(String? theName) {
    String? name = theName;

    mutable Sequence<Person> ourPeople;
    Sequence<Person>? annotations =
        getType().annotations(Person);
    if (exists annotations)  { 
       ourPeople := annotations;
    }
    else {
        // throw Exception("Not annotated people");
    }
    
    doc "The greeting"
    package String greeting() {
        if (exists name) {
            // Note that we use this.first because first() refers to
            // the top-level method first().
            if (exists Person person = this.first(name)) {
                return "" person.lang.hello ", "
                    person.firstName " " person.lastName "!";
                }
                else {
                    return "Hello, " name "!";
                }
        }
        else {
            return "Hello, World!";
        }
    }
    
    Person? first(String firstName) {
        if (exists annotations) {
            mutable Iterator<Person> iter = annotations.iterator();
            while (exists Person anno = iter.head()) {
                if (anno.firstName == firstName) {
                    return anno;
                }
                iter := iter.tail();
            }
        }
        return null;
    }
}
