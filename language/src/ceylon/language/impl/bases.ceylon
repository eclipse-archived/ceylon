shared abstract class BaseIterable<Element,Absent>()
        extends Object()
        satisfies Iterable<Element,Absent>
        given Absent satisfies Null {}

shared abstract class BaseIterator<Element>()
        extends Object()
        satisfies Iterator<Element> {}

shared abstract class BaseMap<Key,Item>()
        extends Object()
        satisfies Map<Key,Item>
        given Key satisfies Object {}

shared abstract serializable
class BaseList<Element>()
        extends Object()
        satisfies SearchableList<Element> {}

shared abstract class BaseCharacterList()
        extends Object()
        satisfies SearchableList<Character> {}

shared abstract serializable
class BaseSequence<Element>()
        extends Object()
        satisfies [Element+] {}
